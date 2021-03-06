// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/4.6.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/4.6.2/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.
firebase.initializeApp({
    'messagingSenderId': '990522297808'
});

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload) {
    console.log('[FIREBASE] Background message received ', payload);
    // Customize notification
    const notificationTitle = 'Shampoo';
    const notificationOptions = {
        body: 'New chat from Shampoo',
        icon: '/img/shiba.png'
    };

    self.addEventListener("message", function(event) {
        event.source.postMessage("Responding to " + event.data);
    });

    return self.registration.showNotification(notificationTitle,
        notificationOptions);
});