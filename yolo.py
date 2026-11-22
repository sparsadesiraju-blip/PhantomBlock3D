# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 18:41:04 2026

@author: D.spandana
"""
import cv2
import matplotlib.pyplot as plt

video_path = r"C:\Users\D.spandana\Videos\temp\6387-191695740_medium.mp4"

cap = cv2.VideoCapture(video_path)

total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))

# Frames we want to inspect
frame_numbers = [0, 85, 170, 255, 340]

for frame_number in frame_numbers:
    cap.set(cv2.CAP_PROP_POS_FRAMES, frame_number)
    ret, frame = cap.read()

    if ret:
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        plt.figure(figsize=(10, 6))
        plt.imshow(frame_rgb)
        plt.title("Frame " + str(frame_number))
        plt.axis("off")
        plt.show()
    else:
        print("Could not read frame:", frame_number)

cap.release()
try:
    from ultralytics import YOLO
    print("Ultralytics is installed")
except ImportError:
    print("Ultralytics is not installed")
    from ultralytics import YOLO
import cv2
import matplotlib.pyplot as plt

video_path = r"C:\Users\D.spandana\Videos\temp\6387-191695740_medium.mp4"

# Load YOLO model
model = YOLO("yolo11n.pt")

# Read one frame
cap = cv2.VideoCapture(video_path)
ret, frame = cap.read()
cap.release()

if ret:
    # Run YOLO
    results = model(frame)

    # Draw detections
    annotated_frame = results[0].plot()

    # Convert BGR → RGB for matplotlib
    annotated_frame = cv2.cvtColor(
        annotated_frame, cv2.COLOR_BGR2RGB
    )

    plt.figure(figsize=(10, 6))
    plt.imshow(annotated_frame)
    plt.axis("off")
    plt.show()

    # Count people
    people_count = 0

    for box in results[0].boxes:
        class_id = int(box.cls[0])

        if class_id == 0: # YOLO class 0 = person
            people_count += 1

    print("👥 People detected:", people_count)

else:
    print("❌ Could not read the video frame")
    result = results[0]
    person_count = 0
    for box in result.boxes:
        class_id = int(box.cls[0])
        if class_id == 0:
            person_count += 1 
            print("people detected: ", person_count)
            
            

import cv2
import matplotlib.pyplot as plt

cap = cv2.VideoCapture(video_path)

total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))

# Five checkpoints across the video
checkpoints = [0, 85, 170, 255, 340]

for target in checkpoints:

    # Start from the beginning and read sequentially
    cap.set(cv2.CAP_PROP_POS_FRAMES, 0)

    frame = None

    for i in range(target + 1):
        ret, frame = cap.read()

        if not ret:
            print("Could not read frame:", target)
            frame = None
            break

    if frame is None:
        continue

    # YOLO detection
    results = model(frame, verbose=False)

    person_count = 0

    for box in results[0].boxes:
        class_id = int(box.cls[0])

        if class_id == 0:
            person_count += 1

    print("Frame", target, "→ People detected:", person_count)

    # Display detected frame
    annotated = results[0].plot()

    plt.figure(figsize=(10, 6))
    plt.imshow(cv2.cvtColor(annotated, cv2.COLOR_BGR2RGB))
    plt.title("Frame " + str(target) +
              " | People detected: " + str(person_count))
    plt.axis("off")
    plt.show()

cap.release()

