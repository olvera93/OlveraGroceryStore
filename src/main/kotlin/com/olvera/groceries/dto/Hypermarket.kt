package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty

enum class Hypermarket(val value: String) {

    @JsonProperty("BILLA") BILLA("BILLA"),
    @JsonProperty("SPAR") SPAR("SPAR"),
    @JsonProperty("LIDL") LIDL("LIDL"),
    @JsonProperty("HDFER") HDFER("HDFER"),
    @JsonProperty("ETSAN") ETSAN("ETSAN"),
    @JsonProperty("OTHER") OTHER("OTHER")

}