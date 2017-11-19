var express = require('express');
var router = express.Router();
var cors = require('cors');
router.use(cors());
var bodyParser = require('body-parser');
router.use(bodyParser());
var databaseManager = require('mongoose');

var port = "";
var databaseName = "gapropro_chat";
var url = "mongodb://localhost:" + port + "/" + databaseName;
var Chat = require('./model/chat');

databaseManager.connect(url);

router.post('/add_new_chat', function (req, res, next) {
    sId = req.body.senderId;
    rId = req.body.receiverId;
    m = req.body.chatMessage;
    var chat = new Chat({
        sender_id: sId,
        receiver_id: rId,
        message: m
    });
    chat.save();
    res.send("Chat is saved");
});

router.post('/get_chat_log', function (req, res, next) {
   sId = req.body.senderId;
   rId = req.body.receiverId;
   Chat.find({$and:[{"sender_id": sId},{"receiver_id": rId}]}, function (err, chatLog) {
       if(err) throw err;
       res.send(chatLog);
   })
});

router.get('/get_all_chats', function(req, res, next) {
    Chat.find({}, function (err, chats) {
        if (err) throw err;
        console.log(chats);
        res.send(chats);
    });
});

module.exports = router;