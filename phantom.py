# -*- coding: utf-8 -*-
import csv
import cv2
import numpy as np

video_path = r"C:\Users\D.spandana\Videos\temp\6387-191695740_medium.mp4"

# -------------------- Configuration --------------------
SAMPLE_EVERY_N_FRAMES = 20
MIN_CONTOUR_AREA = 500
DIFF_THRESHOLD = 30
MORPH_KERNEL_SIZE = 7
ROWS, COLS = 3, 3

# Existing thresholds retained
def congestion_status(count):
    if count <= 2:
        return "LOW"
    elif count <= 4:
        return "MODERATE"
    elif count <= 6:
        return "HIGH"
    return "VERY HIGH"

# Open video
cap = cv2.VideoCapture(video_path)
if not cap.isOpened():
    raise SystemExit(f"Could not open video: {video_path}")

total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
fps = cap.get(cv2.CAP_PROP_FPS)
print(f"Video opened | frames={total_frames} | fps={fps:.2f}")

# -------------------- Build median background --------------------
background_samples = []
for frame_index in range(0, total_frames, SAMPLE_EVERY_N_FRAMES):
    cap.set(cv2.CAP_PROP_POS_FRAMES, frame_index)
    ret, sampled_frame = cap.read()
    if not ret:
        continue
    gray = cv2.cvtColor(sampled_frame, cv2.COLOR_BGR2GRAY)
    background_samples.append(gray)

if not background_samples:
    cap.release()
    raise SystemExit("Could not read any frames to construct a background.")

background = np.median(np.stack(background_samples, axis=0), axis=0).astype(np.uint8)
print(f"Background built from {len(background_samples)} sampled frames.")

# Optional: press any key in the background window to inspect the model.
# Comment these two lines out if you want to go straight into live playback.
cv2.imshow("PhantomBlock - Median Background", background)
cv2.waitKey(500)
cv2.destroyWindow("PhantomBlock - Median Background")

# -------------------- Process video frames --------------------
kernel = np.ones((MORPH_KERNEL_SIZE, MORPH_KERNEL_SIZE), np.uint8)
cap.set(cv2.CAP_PROP_POS_FRAMES, 0)
frame_index = -1
last_map = None
last_counts = None
last_total = 0

# Display sampled frames at approximately their original playback speed.
# Since only every Nth frame is shown, the delay is scaled accordingly.
playback_delay_ms = max(1, int(1000 * SAMPLE_EVERY_N_FRAMES / fps)) if fps > 0 else 33
csv_file = open(
    "phantomblock_video_observations.csv",
    "w",
    newline="",
    encoding="utf-8"
)

csv_writer = csv.writer(csv_file)

csv_writer.writerow([
    "Frame",
    "TimeSeconds",
    "Area1", "Area2", "Area3",
    "Area4", "Area5", "Area6",
    "Area7", "Area8", "Area9",
    "TotalContours"
])
print("Live analysis started. Press Q or ESC to quit; press P to pause/resume.")
paused = False

while True:
    ret, frame = cap.read()
    if not ret:
        break
    frame_index += 1

    # Match the sampling interval used by the background pipeline.
    if frame_index % SAMPLE_EVERY_N_FRAMES != 0:
        continue

    gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    difference = cv2.absdiff(gray_frame, background)
    _, foreground_mask = cv2.threshold(
        difference, DIFF_THRESHOLD, 255, cv2.THRESH_BINARY
    )
    foreground_mask = cv2.morphologyEx(
        foreground_mask, cv2.MORPH_OPEN, kernel
    )
    foreground_mask = cv2.morphologyEx(
        foreground_mask, cv2.MORPH_CLOSE, kernel
    )

    contours, _ = cv2.findContours(
        foreground_mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE
    )

    frame_height, frame_width = gray_frame.shape
    area_counts = [0] * (ROWS * COLS)
    accepted_contours = []

    # Count each qualifying contour exactly once, assigned by its centroid.
    for contour in contours:
        area = cv2.contourArea(contour)
        if area <= MIN_CONTOUR_AREA:
            continue

        x, y, w, h = cv2.boundingRect(contour)
        center_x = x + w // 2
        center_y = y + h // 2

        col = min(center_x * COLS // frame_width, COLS - 1)
        row = min(center_y * ROWS // frame_height, ROWS - 1)
        region_index = row * COLS + col

        area_counts[region_index] += 1
        accepted_contours.append((x, y, w, h, region_index, area))

    # This is the total of accepted foreground contours, used as a proxy.
    # A contour is not always one person: nearby people can merge into one blob.
    total_detected = sum(area_counts)

    # Draw grid and labels over the original colour frame.
    congestion_map = frame.copy()
    region_width = frame_width / COLS
    region_height = frame_height / ROWS

    colour_by_status = {
        "LOW": (0, 255, 0),          # green
        "MODERATE": (0, 255, 255),   # yellow
        "HIGH": (0, 165, 255),       # orange
        "VERY HIGH": (0, 0, 255),    # red
    }

    # Add a dark top banner for legible summary text.
    banner_height = 42
    cv2.rectangle(congestion_map, (0, 0), (frame_width, banner_height), (25, 25, 25), -1)
    cv2.putText(
        congestion_map,
        f"TOTAL DETECTED CONTOURS: {total_detected}",
        (10, 28), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 255, 255), 2,
        cv2.LINE_AA
    )

    for row in range(ROWS):
        for col in range(COLS):
            region_index = row * COLS + col
            count = area_counts[region_index]
            status = congestion_status(count)
            colour = colour_by_status[status]

            x1 = int(round(col * region_width))
            y1 = int(round(row * region_height))
            x2 = int(round((col + 1) * region_width))
            y2 = int(round((row + 1) * region_height))

            cv2.rectangle(congestion_map, (x1, y1), (x2, y2), colour, 3)

            # Text backing makes labels readable on bright/dark video areas.
            label = f"Area {region_index + 1}: {count} | {status}"
            (text_w, text_h), baseline = cv2.getTextSize(
                label, cv2.FONT_HERSHEY_SIMPLEX, 0.48, 1
            )
            label_y = min(y1 + 23, frame_height - 5)
            label_top = max(0, label_y - text_h - 5)
            cv2.rectangle(
                congestion_map,
                (x1 + 2, label_top),
                (min(x1 + text_w + 10, frame_width - 1), label_y + baseline),
                (25, 25, 25), -1
            )
            cv2.putText(
                congestion_map, label, (x1 + 5, label_y),
                cv2.FONT_HERSHEY_SIMPLEX, 0.48, (255, 255, 255), 1,
                cv2.LINE_AA
            )
            
    last_map = congestion_map
    last_counts = area_counts
    last_total = total_detected

    time_seconds = frame_index / fps if fps > 0 else 0

    csv_writer.writerow([
        frame_index,
        round(time_seconds, 3),
        *area_counts,
        total_detected
    ])

    csv_file.flush()

    # Show the video and analysis overlay continuously.
    cv2.imshow("PhantomBlock - Live Regional Congestion Map", congestion_map)

    print(
        f"Frame {frame_index}: regional contour counts={area_counts} | "
        f"total detected contours={total_detected}"
    )

    # Keyboard controls: Q/ESC quit, P pause/resume.
    while paused:
        key = cv2.waitKey(30) & 0xFF
        if key in (ord("p"), ord("P")):
            paused = False
        elif key in (ord("q"), ord("Q"), 27):
            paused = False
            running = False
            break


    key = cv2.waitKey(playback_delay_ms) & 0xFF
    if key in (ord("q"), ord("Q"), 27):
        break
    elif key in (ord("p"), ord("P")):
        paused = True

cap.release()
cv2.destroyAllWindows()

if last_map is None:
    raise SystemExit("No frames were processed.")

print("\nFinal processed frame:")
for index, count in enumerate(last_counts, start=1):
    print(f"Area {index}: {count} contours -> {congestion_status(count)}")
print(f"Total detected contours: {last_total}")

print("Live analysis closed.")
csv_file.close()
print("CSV file created: phantomblock_video_observations.csv")
