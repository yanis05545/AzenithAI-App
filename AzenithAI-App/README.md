# Azenith-AI Companion

App pendamping untuk modul root **Azenith-AI Gaming Booster**. Cuma satu
aplikasi, tanpa AndroidX/library pihak ketiga, dengan icon AZT. Struktur ini
dibuat supaya kamu bisa kerja **hanya pakai Termux (edit kode + git) dan
GitHub (yang build APK-nya)** — tidak perlu Android Studio dan tidak perlu
build APK di HP sama sekali.

## Cara kerja

```
Termux (edit kode, git commit, git push)
        │
        ▼
   GitHub repo
        │  (trigger otomatis)
        ▼
GitHub Actions (compile APK di server, GRATIS untuk repo publik)
        │
        ▼
Release "latest" -> link APK siap diunduh & diinstal di HP
```

Kamu tidak pernah menjalankan Gradle/Android SDK di Termux. Termux hanya
dipakai sebagai editor + client Git.

## 1) Setup sekali di Termux

```bash
pkg update && pkg upgrade -y
pkg install git -y
termux-setup-storage

git config --global user.name "yanis"
git config --global user.email "kamu@example.com"
```

Buat Personal Access Token di GitHub (Settings > Developer settings >
Personal access tokens > Fine-grained token, scope **Contents: Read and
write** untuk repo ini saja) supaya `git push` dari Termux tidak perlu
password setiap kali.

## 2) Clone repo

Kalau repo sudah dibuat di GitHub (upload folder ini sekali lewat web GitHub,
atau `git init` dari sini):

```bash
cd ~
git clone https://github.com/USERNAME/AzenithAI-App.git
cd AzenithAI-App
```

Kalau mulai dari folder ini (belum ada di GitHub):

```bash
cd AzenithAI-App
git init
git add .
git commit -m "init: Azenith-AI Companion"
git branch -M main
git remote add origin https://github.com/USERNAME/AzenithAI-App.git
git push -u origin main
```

Saat diminta password, tempel **token** dari langkah 1 (bukan password akun).

## 3) Build APK otomatis

Begitu `git push` ke branch `main`, buka tab **Actions** di repo GitHub-nya -
build jalan otomatis (~3-5 menit). Setelah selesai:

- APK ada di tab **Releases** repo, rilis bernama **"latest"** -> link ini
  **selalu sama** walau kamu push update berkali-kali, jadi cukup bookmark
  sekali di HP.
- Buka link itu dari HP, unduh `AzenithAI-Companion.apk`, instal
  (aktifkan "Izinkan dari sumber ini" kalau diminta).

## 4) Ubah kode dari Termux

Edit file apa saja pakai editor teks di Termux (`nano`, `vim`, atau Acode/
editor lain yang bisa akses storage), misalnya:

```bash
nano app/src/main/java/com/azenithtech/azenithai/MainActivity.kt
```

Lalu:

```bash
git add -A
git commit -m "jelaskan perubahannya"
git push
```

GitHub Actions otomatis build ulang dan memperbarui rilis "latest" - tidak
ada langkah manual lain.

## Struktur project

```
AzenithAI-App/
├── app/
│   ├── build.gradle.kts         # konfigurasi build modul app (1 APK, tanpa split ABI)
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/azenithtech/azenithai/MainActivity.kt
│       └── res/
│           ├── layout/activity_main.xml
│           ├── values/ (strings.xml, colors.xml, themes.xml)
│           └── mipmap-*/ic_launcher*.png   # icon AZT, sudah di-generate semua densitas
├── build.gradle.kts             # root build script
├── settings.gradle.kts
├── gradle.properties
└── .github/workflows/build.yml  # build APK otomatis di GitHub Actions
```

## Kenapa cuma 1 aplikasi & tanpa dependency tambahan

- `app/build.gradle.kts` sengaja mematikan `splits` per-ABI/density -> selalu
  **satu file APK** untuk semua HP, bukan beberapa varian.
- Tidak ada AndroidX/Compose/library luar lain - cuma Android SDK bawaan.
  Build jadi ringan, cepat, dan tidak ada risiko versi dependency yang basi
  atau bentrok di kemudian hari - sesuai permintaan: satu aplikasi yang
  stabil, bukan banyak modul yang harus terus-menerus diperbaiki.
- Build selalu dari server GitHub yang bersih (image Ubuntu baru tiap kali),
  jadi hasilnya konsisten - tidak tergantung kondisi penyimpanan/HP kamu.

## Icon

Icon launcher diambil dari logo AZT (bagian huruf AZT + bingkai neon, tanpa
baris handle TikTok/Telegram supaya tetap jelas di ukuran kecil), sudah
di-generate ke semua densitas (`mipmap-mdpi` sampai `mipmap-xxxhdpi`).

## Root

App ini menjalankan perintah lewat `su -c azenith-ai ...` - sama seperti
`system/bin/azenith-ai` dipanggil manual dari terminal. Pastikan app ini
diberi izin root di Magisk/KernelSU/APatch Manager pada pemakaian pertama.
