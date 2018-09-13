package com.iot.kotlin.model

import com.google.gson.annotations.SerializedName


/**
 * Class which provides a model for the list of fixture in each room from http://private-1e863-house4591.apiary-mock.com/rooms
 * @property rooms the class for the top node of the json
 * @property bedroom the field in rooms
 * @property livingroom the field in rooms
 * @property kitchen the field in rooms
 * @property fixtures the mutablelist of string for the fixtures in each room
 */

class SmartRooms {

    var rooms: Rooms? = null
    inner class Rooms {

        @SerializedName("Bedroom")
        var bedroom: Rm? = null
        @SerializedName("Living Room")
        var livingroom: Rm? = null
        @SerializedName("Kitchen")
        var kitchen: Rm? = null
    }

    inner class Rm {

        @SerializedName("fixtures")
        var fixtures: MutableList<String>? = null
    }


}



