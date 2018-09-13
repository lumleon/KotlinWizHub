# KotlinWizHub
 
The Android app written in Kotlin which demonstrates how to use switch button to control turning ON/OFF of smart home fixtures. 

A) Problem Statement

Mary has turned several rooms of her house into an IoT enabled smart home and hope to have an Android application to turn on and off the fixtures.
She has some expectations for requirements, listed below:

1) App Functionality

1.1 Create a homepage that lists all of the rooms. 
1.2 For each room, create a page that lists all the fixtures and their current state (on/off), as well as the ability to turn each fixture on or off.
1.3 The AC fixture has special requirements. The application needs to ping a weather API periodically for the temperature. If the temperature is above 25°C, 
    then the AC needs to turn on immediately if it is off. If the temperature is below 25°C, then the AC should be turned off if it is on. 
1.4 The AC fixture should also show the temperature from the weather API as well as the fixture’s on/off status.

2) Persistence Requirements

2.1 She would like you to locally persist the data for rooms, fixtures and their state within the app. 
2.2 This persistence should allow the app to show the last known state of the house even if the user is offline.

3) Performance Requirements

3.1 She would like the UI to be seamlessly updated instantly without any blocking or lag when a state is confirmed changed. 
3.2 When a request is in flight (that is, request is sent but response not yet received), the user should still be able to navigate to other screens. When navigating to a room page, it should immediately show the fixtures’ current state.

4) Architecture Requirements

4.1 She wants you to properly engineer the app and develop a solid architecture to operate these fixtures. You can use MVVM, MVC or any other architecture that you find useful, but must document it in your README.

B) API reference
She has setup a backend infrastructure API here: http://private-1e863-house4591.apiary-mock.com/. (Please visit for the full API list).

The API has the following calls:

API Call	Return value
/rooms	JSON format of all rooms and fixtures in the house
/:room/:fixture/on	sets the status of the fixture to “ON” and returns true
/:room/:fixture/off	sets the status of the fixture to “OFF” and returns true

Weather API	URL
MetaWeather API for Hong Kong	(to prevent from being blocked, do not call more than once a minute) https://www.metaweather.com/api/location/2165352/
To make it easy for you to test, the mock API also has 2 calls that return weather temperatures that turn the AC on and off (you may use them to simulate weather conditions for testing):

http://private-1e863-house4591.apiary-mock.com/weather/cold

// returns mirrored structure of metaweather
 consolidated_weather: 
     0: 
        the_temp: 20
http://private-1e863-house4591.apiary-mock.com/weather/hot

// returns mirrored structure of metaweather
 consolidated_weather: 
     0: 
        the_temp: 30

C) Solution

In order to achieve the expectation of Mary, her requirements were investigated thoroughly and explained below:

1) App functionality
1.1 Recycler view is used as the homepage to list all rooms of her house as it is flexible to add extra room by a floating action button. 
  
1.2 Each room is clickable to show another activity that implements recycler view to list the fixtures, on/off state from a switch widget and temperature for "AC" fixture.
    The switch widget is enabled to send API request to server for turning on/off the fixture.

1.3 The turn on or off of "AC" is achieved by the job scheduler function implemented in the main activity. It scheduled an API call to the Metaweather API
    and get the latest temperature every 30 minutes. The latest temperature is persisted locally using Shared Preference and the change of temperature is monitored
    by onSharedPreferenceChangelistener. When Mary first opens the app, she can schedule the "AC" on/off from the option menu item. Job scheduler
    will ignite a job to check Hong Kong temperature and send API request to server to turn on/off the "AC" if local data   shows it is off and vice versa. 

If Mary opens the app and access the fixture list, the app will also check the local data and perform API request to control on/off of the "AC"

1.4 As the temperature is persisted locally by shared preference, the value is accessed and shown next to "AC" fixture. 
 
2) Data Persistence

2.1 The method of data persistene is studied for shared preference and SQLite database. Since the main feature of this app is to control on/off of fixture,
    shared perference is chosen for the consideration of its feature of key value pairs storage. In ddition, the JSON of the backend API shows minimum
    information of room and fixtures only. I believe shared preference is more flexible to maintain and easy to implement for data persistence.
2.2 In scheduled job, the app is able to update the key value in shared preference, making Mary able to access last know data when she is offline.


3) Performance Requirements

3.1 The update of state depends on the result of the API and condition of the local fixture state. For seamlessly updated instantly without any blocking or lag, Retrofit and RxJava2 classes are used to 
    call the API. Since it operates in background thread, Mary still can navigate to another room as the classes will handle the result asychronously. 
3.2 This requirement is also fulfilled by Retrofit  + RxJava2

4)Architecture Requirements

MVVM is introduced by Google in 2017 which is usually implemented with Architecture Components (Lifecycle, LiveData, ViewModel, Room Persistence). 
Since I'm new to MVVM and have chosed shared preference as the data persistence, I would prefer to implement this solution with MVC architecture. 

4.1 Model:  the data layer - responsible for managing the business logic and handling network or database API.

4.1.1) Shared preference
    
   a. Fixturekey - status value pairs, e.g. “bedroom_ac”, "on" // showing the switch widget status in fixture activity
   b. RoomKey - fixturelist value pairs, e.g “bedroom”, "light1, light2, ac" // showing  the list of fixtures in the room
   c. theTempKey - temperature value pair e.g "theTemp", "28.81"  // showing the temperature next the the "AC" fixture     

4.2.1) Data class
   a. MetaWeather -  the data class of the json tree returned from the Metaweather API
   b. Smartroom   - the data class of the json tree returned from the mock API

4.2) View: the UI layer - a visualisation of the data from the Model.

4.2.1) Main Activity - The view that displays the list of room defined in the app. Regarding to the Json result of the room API, list of room
is given in the app rather than parsed in the JSON result.  
4.2.2) Fixture Activity -  The view that displays the list of fixture and its state from the model in shared preference

4.3) Controller:  logic layer  -  gets notified of the user’s behavior and updates the Model as needed.

4.3.1) ScheduleAConOff (Option menu item) - it allows user to schedule job to monitor the current temperature and control on/off of "AC". It also updates the data model in shared preference.
4.3.2) onOffSwith widget - it allows user to switch on or off the fixture. Consequencely, it updates the data model, so that the view will update the UI.
4.3.3) onSharedPreferenceChange listener - it is an automatic behaviour that monitor the temperature model and control the on/off of "AC"

D) How to test/build/use your solution. 

1.0) Test:   Unit test and Espresso UI test is done that integrated to Google Cloud Testing functionality. However, manual testing of the app functionality is also suggested for user experience. 

2.0) build: android phone / emulator with minimum sdk version 23 is required for installation   
    
3.0) use: The home page shows the room list which allows user to click into the fixture list. The fixture list shows the name and the on/off state of fixture on the swith button. It also allows user to switch on/off the fixtures.
    
 
E) Trade-offs, things done good and suggestion on the project.

1.0) Trade-offs
    MVC is easy to implement but difficult to write unit test.
    
2.0) things done good

2.1) The job schedule function that can get the current temperature and control on/off status of the AC.
2.2)  Use of onSharedpreferenceChangelistener to change the Switch widget state to reflect latest state of fixtures, which save a lot of programming work on data persistence while achieveing the purpose of the app.   

3.0) Suggestion  

3.1) Display a progress bar when fetching the room list API and disable it when the process is completed
3.2) Find a method to list the bedroom, living-room and kitchen from the JSON instead of written in the app 
3.3) Restructure the app to MVVM architecture and use LiveData, Room persistence to compare the performance.
3.4) Study unit test metod based on the MVVM architecture and see is the tesing as easy as the architecture claims.
    

