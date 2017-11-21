var express = require('express');
var router = express.Router();

function MongoManager() {
    this.port = "";
    this.databaseName = "gapropro_chat";
    this.url = "mongodb://localhost:" + this.port + "/" + this.databaseName;
    this.mongoose = require('mongoose');
    this.mongoose.connect(this.url);
}

var Chat = require('./chat');
var mongoManager = new MongoManager();

mongoManager.addNewChat = function(senderId, receiverId, chatMessage) {
    var chat = new Chat({
        sender_id: senderId,
        receiver_id: receiverId,
        message: chatMessage
    });
    chat.save();
    return("Chat is saved.");
};

mongoManager.getChatLog = function(firstId, secondId) {
    var results = Chat.find({$or:[{$and:[{"sender_id": firstId},{"receiver_id": secondId}]}, {$and:[{"sender_id": secondId},{"receiver_id": firstId}]}]}).sort({date: 'asc'}).exec(function (err, chatLog) {
        if(err) throw err;
        return chatLog;
    });
    results;
};

module.exports = mongoManager;