package com.example.carebear.models

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var friendIds: List<String> = emptyList(),
    var friendRequests: List<FriendRequest> = emptyList(),
    var friends: List<Friend> = emptyList(),
    var chats: List<ChatMembership> = emptyList(),
    var username: String? = null, // Nullable
    var bio: String? = null       // Nullable
) {
    // Secondary constructor without `username` and `bio`
    constructor(
        id: String = "",
        name: String = "",
        email: String = "",
        friendIds: List<String> = emptyList(),
        friendRequests: List<FriendRequest> = emptyList(),
        friends: List<Friend> = emptyList(),
        chats: List<ChatMembership> = emptyList()
    ) : this(
        id = id,
        name = name,
        email = email,
        friendIds = friendIds,
        friendRequests = friendRequests,
        friends = friends,
        chats = chats,
        username = null,
        bio = null
    )
}
