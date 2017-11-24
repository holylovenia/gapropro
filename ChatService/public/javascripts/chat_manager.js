/**
 * Chat Manager
 * Manages chat sending and history
 * Main route : /chat
 */

var express = require('express');
var router = express.Router();
var cors = require('cors');
var bodyParser = require('body-parser');
var request = require('request');
router.use(cors());
router.use(bodyParser());

var databaseManager = require('./model/mongo_manager');
var Chat = require('./model/chat');
var Firebase = require('./model/firebase');

/**
 * [ Add New Chat ]
 * Mengirimkan chat kepada user lain
 * Input : senderId, receiverId, chatMessage
 */
router.post('/add_new_chat', function (req, res, next) {
    var sId = parseInt(req.body.senderId);
    var rId = parseInt(req.body.receiverId);
    var m = req.body.chatMessage;
    var token = "";
    Firebase.findOne({'user_id': rId}, function (err, firebase) {
        if (err) throw err;
        token = firebase.fb_token;

        var options = {
            method: 'post',
            json: true,
            url: "https://fcm.googleapis.com/fcm/send",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "key=AIzaSyDLWhhAQ3MmOPvhwXCny7kLw_HyWWq2A6g"
            },
            body: {
                "data": {
                    "notification": {
                        "type": "message",
                        "body": req.body.chatMessage,
                        "title": "New Message"
                    }
                },

                "to": token
            }
        };

        request(options, function (err, res, body) {
            if (err) {
                console.log('Error :', err);
                return true;
            }
        });
    });


    var chat = new Chat({
        sender_id: sId,
        receiver_id: rId,
        message: m
    });
    chat.save();

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

/**
 * [ Get Chat Log ]
 * Mengambil chat log user
 * Input : firstId, secondId
 */
router.post('/get_chat_log', function (req, res, next) {
    var fId = req.body.firstId;
    var sId = req.body.secondId;
    Chat.find({$or: [{$and: [{"sender_id": fId}, {"receiver_id": sId}]}, {$and: [{"sender_id": sId}, {"receiver_id": fId}]}]}, '-_id -__v', function (err, chatLog) {
        if (err) throw err;

        var returnObj = {};
        returnObj.result = chatLog;


        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(returnObj));
    });
});

/**
 * [ Get All Chats ]
 * Mengambil data semua chat
 * Input : -
 */
router.get('/get_all_chats', function (req, res, next) {
    Chat.find({}, '-_id -__v', function (err, chats) {
        if (err) throw err;
        var returnObj = {};
        returnObj.result = chats;

        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(returnObj));
    });
});

/**
 * [ Finish Chat ]
 * Mengirimkan notifikasi kepada driver jika user sudah finish order.
 * Input : receiverId
 */
router.post('/finish_chat', function (req, res) {
    var rId = parseInt(req.body.receiverId);
    var token = "";
    Firebase.findOne({'user_id': rId}, function (err, firebase) {
        if (err) throw err;
        token = firebase.fb_token;

        var options = {
            method: 'post',
            json: true,
            url: "https://fcm.googleapis.com/fcm/send",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "key=AIzaSyDLWhhAQ3MmOPvhwXCny7kLw_HyWWq2A6g"
            },
            body: {
                "data": {
                    "notification": {
                        "type": "close"
                    }
                },
                "to": token
            }
        };

        request(options, function (err, res, body) {
            if (err) {
                console.log('Error :', err);
                return true;
            }
            console.log("Finish Chat " + body);
        });

        res.set('Content-Type', 'application/json');
        res.send('{"status":true}');
    });
});

module.exports = router;
