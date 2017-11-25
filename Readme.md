# Tugas 3 IF3110 Pengembangan Aplikasi Berbasis Web

Melakukan upgrade Website ojek online sederhana pada Tugas 2 dengan mengaplikasikan ***cloud service* (Firebase Cloud Messaging) dan *framework* MEAN stack**.

**Luangkan waktu untuk membaca spek ini sampai selesai. Kerjakan hal yang perlu saja.**

## Anggota Tim

- Rionaldi Chandraseta / 13515077
- Vincent Hendryanto Halim / 13515089
- Holy Lovenia / 13515113

## Penjelasan Mengenai Tugas

### Tujuan Pembuatan Tugas

Diharapkan dengan tugas ini Anda dapat mengerti:
* MEAN stack (Mongo, Express, Angular, dan Node)
* *Cloud service* Firebase Cloud Messaging (FCM) dan kegunaannya.
* Web security terkait access token dan HTTP Headers.

### Arsitektur Umum

Tugas 3 ini terdiri dari komponen Tugas 2 dan tambahan yang harus dibuat:
* `Pemanfaatan FCM`: Pengiriman pesan dari pelanggan ke driver atau sebaliknya dengan menggunakan layanan Firebase Cloud Messaging (FCM).
* `Implementasi MEAN stack`: Membuat REST service untuk keperluan layanan chat memanfaatkan FCM menggunakan Node, Express dan Mongo, serta membuat halaman chat yang menggunakan Angular.

![](img/arsitektur_umum.png)

### Deskripsi Tugas

Kali ini, Anda diminta untuk merubah sedikit fungsionalitas order yang sudah ada dan menambah fungsionalitas chat pada aplikasi yang telah anda buat pada tugas 2. Aplikasi ini akan menggunakan MEAN stack untuk halaman chat dan REST service, serta menggunakan layanan cloud Firebase Cloud Messaging sebagai media penyampaian pesan. Selain itu, Anda juga diminta untuk mengimplementasikan beberapa fitur security. Spesifikasi untuk tugas ini adalah sebagai berikut:

1. Halaman Order akan bergantung pada status pengguna, apakah driver atau bukan.
2. Bila status pengguna merupakan driver maka pada halaman order akan ditampilkan fitur finding order dimana akan membuat driver visible ketika user ingin melakukan order pada halaman select driver. Apabila driver tidak melakukan finding order, maka driver tidak akan ditampilkan pada halaman select driver ketika pengguna non driver melakukan order.
3. Pengguna dapat saling bertukar pesan dengan driver secara realtime di halaman order chat driver. Fitur ini harus diimplementasikan dengan MEAN stack, di mana fitur halmaman chat harus diimplementasikan dengan Angular dan fitur REST service diimplementasikan menggunakan Node, Express, dan Mongo.
4. REST service yang akan menghubungkan client dan Firebase Cloud Messaging. Rincian service ini akan dijelaskan kemudian. Silahkan pelajari cara mendaftar ke layanan Firebase, dan cara membuat project baru.
5. Pengguna harus login terlebih dahulu sebelum dapat melakukan chat. Silahkan cari cara untuk sharing session antara JSP dan MEAN. Sebagai contoh, anda dapat menggunakan cookie yang dapat diakses oleh keduanya.
6. Pada halaman order select driver, aplikasi hanya menampilkan driver yang **sesuai requirement** saja (lihat poin 7). Ketika nama driver dipilih, maka akan muncul kotak chat antar pengguna non driver dan driver pada tab chat driver di halaman order.
7. Requirement yang dimaksud ialah driver memiliki preferred location yang dituju pengguna, online, **sedang melakukan finding order**, dan tidak sedang mendapat order.
8. Pengguna dengan IP address yang berbeda tidak dapat menggunakan access token yang sama.
9. Pengguna dengan user-agent yang berbeda tidak dapat menggunakan access token yang sama. Dalam hal ini, user-agent yang dimaksud adalah web browser yang digunakan.
10. Komponen yang harus digunakan pada AngularJS adalah:
    * Data binding (ng-model directives)
    * Controllers (ng-controllers)
    * ng-repeat, untuk menampilkan list
    * $http untuk AJAX request
    * $scope untuk komunikasi data antara controller dengan view.
    * ng-show dan ng-hide untuk menampilkan/menyembunyikan elemen
11. Tidak perlu memperhatikan aspek keamanan dan etika dalam penyimpanan data.

### Rincian Arsitektur Aplikasi Chat

![](img/mekanisme_chat.png)

Proses untuk komunikasi antar client adalah sebagai berikut:
1. Ketika client dijalankan, client akan meminta token (token yang berbeda dengan token untuk authentication dari Identity Service) dari FCM.
2. FCM mengirimkan token ke client.
3. Setelah token diterima, client akan mengirim token serta identitas dari client (nama/email) ke chat server. Identitas client digunakan untuk mengidentifikasi kepemilikan token.
4. Untuk mengirim pesan kepada client lain, client pertama mengirimkan pesan yang berisi identitas pengirim, identitas tujuan, dan isi pesan ke chat server.
5. Chat server kemudian akan mencari token yang terkait dengan identitas tujuan.
6. Chat server lalu mengirim request ke FCM untuk mengirimkan pesan kepada client dangan token yang terkait.
7. FCM mengirimkan pesan kepada tujuan.


## Pembagian Tugas

Chat App Front-end :
1. Mengubah list driver yang bisa dipilih: 13515113
2. Menambahkan halaman chat untuk driver: 13515089, 13515077, 13515113
3. Menambahkan halaman chat untuk user: 13515089, 13515077
4. Mengubah halaman order: 13515089
5. Mengambil firebase token: 13515077, 13515089

Chat REST Service:  
1. Menambahkan chat baru: 13515077, 13515089, 13515113
2. Memberikan chat log antar 2 pengguna: 13515113
3. Memberikan daftar pengguna yang available: 13515113
4. Mengubah status online pengguna: 13515113
5. Mengubah status finding order pengguna: 13515113
6. Menginisiasi chat: 13515077, 13515089
7. Menyelesaikan chat: 13515077, 13515089

Fitur security (IP, User-agent) :
1. Penambahan User-Agent dan IP Address pada access token: 13515077, 13515113
2. Pengecekan User-Agent dan IP Address pada access token: 13515077
3. Pengubahan login, register, dan logout: 13515113


## About

Tim GaProPro

Rio | Vincent | Holy