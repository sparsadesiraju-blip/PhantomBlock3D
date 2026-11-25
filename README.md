PhantomBlock - A Campus Congestion Simulation and Visualisation System

PhantomBlock is a campus congestion modelling and visualisation project designed to study pedestrian movement and congestion patterns within a university building.
The project combines Java-based simulation, computer vision, database storage, data analysis, and 3D visualisation to explore how congestion develops across different areas and time periods.

 Project Objectives
- Simulate pedestrian movement within a multi-floor university building.
- Analyse crowd density and congestion patterns.
- Detect and track people in video footage using computer vision.
- Divide video frames into nine grid regions to analyse regional people counts and congestion rates.
- Store collected data in an SQL database.
- Export and analyse data through Excel graphs.
- Visualise building activity through an interactive 3D environment.

Building Model
The simulated building includes:
- 7 floors
- 70 classrooms
- Laboratory spaces
- Two canteens
- Separate washroom facilities
- Passenger elevators
- A separate faculty elevator
- offices

Technologies Used
 Technology          | Purpose 
 Java                | Building and pedestrian simulation 
 Python              | Video processing and people detection 
 OpenCV              | Video frame processing 
 YOLO                | Person detection 
 ByteTrack           | Object tracking |
 SQL                 | Data storage |
 Ms Excel            | Data analysis and graph generation 
 HTML,CSS,JavaScript | Application interface 
 Three.js            | 3D building visualisation 

System Workflow
1. Video footage is processed using Python and computer vision.
2. Each video frame is divided into nine grid regions.
3. People counts and congestion rates are calculated for the regions.
4. The collected data is stored in an SQL database.
5. Data is transferred to Excel for analysis and graph generation.
6. Graphs and visualisations are displayed through the PhantomBlock application.
7. A separate Java simulation models pedestrian movement and elevator behaviour.

Project Structure
The project contains multiple components:
- Java Simulation: Models the building, floors, students, and elevators.
- Computer Vision: Processes video footage and detects people.
- Database: Stores grid-wise people counts and congestion rates.
- Data Analysis: Uses Excel to generate graphs.
- 3D Visualisation: Displays the building and congestion patterns using Three.js.

 Current Development Status
PhantomBlock is an ongoing prototype. Current work includes developing the simulation, validating video-based observations across building floors, and improving the integration between data processing and 3D visualisation.

 Future Scope
- Collection and validation of footage across all floors.
- Improved camera calibration and spatial mapping.
- More detailed pedestrian movement modelling.
- Stronger integration between simulation and video-derived observations.
- Further development towards a campus digital twin.

Project
Project Name: PhantomBlock
Status: Prototype / Ongoing Development
