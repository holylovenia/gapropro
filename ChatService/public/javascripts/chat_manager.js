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

router.post('/add_new_chat', function (req, res, next) {
    var sId = parseInt(req.body.senderId);
    var rId = parseInt(req.body.receiverId);
    var m = req.body.chatMessage;

    // If sending succeeds, save to mongo
    var chat = new Chat({
        sender_id: sId,
        receiver_id: rId,
        message: m
    });
    chat.save();

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
