package com.example.carebear.models

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var friendIds: List<String> = emptyList(),
    var friendRequests: List<FriendRequest> = emptyList()
)