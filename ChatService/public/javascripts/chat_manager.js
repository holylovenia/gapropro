/**
 * Chat Manager
 * Manages chat sending and history
 * Main route : /chat
 */

var express = require('express');
var router = express.Router();
var cors = require('cors');
var bodyParser = require('body-parser');
router.use(cors());
router.use(bodyParser());

var databaseManager = require('./model/mongo_manager');
var Chat = require('./model/chat');
var Firebase = require('./model/firebase');

router.post('/add_new_chat', function (req, res, next) {
    var sId = parseInt(req.body.senderId);
    var rId = parseInt(req.body.receiverId);
    var m = req.body.chatMessage;

    var token = Firebase.findOne({'user_id': rId}, function (err, firebase) {
       if (err) throw err;
    });

    var options = {
        method: 'post',
        json: true,
        url: "https://fcm.googleapis.com/fcm/send",
        headers: {
            "Content-Type": "application/json",
            "Authorization:": "key=AAAA5p-6zdA:APA91bFLGzZ2w0jSjpU_g3BQ18vV6XQenkolJUYa1EnjtABldwvl82vMq_SBYJw8Jw6zL8PeubIDaDCjhFqmSTNkDqkLboWuVQEQodLYq3v5B8JLubWiu8d2mOEeRZRK5NwhocUeaMvA"
        },
        body: {
            "data": "INCOMING",
            "to": token
        }
    };

    request(options, function (err, res, body) {
        if (err) {
            console.log('Error :', err);
            return
        }
        console.log(' Body :', body);
        var chat = new Chat({
            sender_id: sId,
            receiver_id: rId,
            message: m
        });
        chat.save();
    });

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

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

router.get('/get_all_chats', function (req, res, next) {
    Chat.find({}, '-_id -__v', function (err, chats) {
        if (err) throw err;
        var returnObj = {};
        returnObj.result = chats;

        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(returnObj));
    });
});

module.exports = router;
