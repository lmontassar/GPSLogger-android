# GPSLogger-android

GPSLogger-android is an Android application that logs GPS data, signal strength, and battery level. It provides a user-friendly interface for tracking location and device information, with support for multiple languages and CSV data export.

## Features

- Real-time GPS tracking (latitude, longitude, altitude)
- Signal strength monitoring
- Battery level tracking
- Multi-language support (English, French, Arabic)
- CSV data logging
- Runtime permission handling for location and phone state access

## Requirements

- Android Studio
- Android SDK (API level 24 or higher)
- Android device or emulator with GPS capabilities

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/lmontassar/GPSLogger-ajdroid.git
2. Open the project in Android Studio.
3. Build and run the application on your Android device or emulator.

## Usage

1. Launch the application on your Android device.
2. Grant the necessary permissions when prompted (location and phone state).
3. The main screen will display:
- Current latitude
- Current longitude
- Current altitude
- Signal strength
- Battery level
4. Use the "Change Language" button to cycle through available languages (English, French, Arabic).
5. Click "Save to CSV" to log the current data to a CSV file.

## File Storage

The CSV log file (LogTracking.csv) is stored in the app's external files directory. The exact file location is displayed in a toast message and in a TextView when saving data.

## Permissions

The app requires the following permissions:

- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `READ_PHONE_STATE`

These permissions are requested at runtime to comply with Android's permission model.

## Project Structure

- `MainActivity.java`: Main activity handling UI, permissions, location updates, and data logging.
- `activity_main.xml`: Layout file for the main activity (not provided in the code snippet).
- `strings.xml`: Resource file for localized strings (not provided, but referenced in the code).

## Contributing

Contributions to GPSLogger-ajdroid are welcome. Please feel free to submit pull requests or create issues for bugs and feature requests.

## License

[Specify your license here, e.g., MIT, Apache 2.0, etc.]

## Author

LOUNISSI MONTASSAR

