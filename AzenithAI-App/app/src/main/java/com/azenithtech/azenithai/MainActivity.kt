package com.azenithtech.azenithai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Azenith-AI Companion
 *
 * App pendamping RINGAN untuk modul root Azenith-AI Gaming Booster.
 * Sengaja tanpa AndroidX/library pihak ketiga - cuma Android SDK biasa -
 * supaya tetap jadi SATU aplikasi yang kecil & stabil, gampang dibuild
 * ulang di GitHub Actions tanpa dependency yang bisa basi/pecah.
 *
 * Semua perintah dijalankan lewat `su -c` (akses root sudah wajib untuk
 * modul ini juga, jadi tidak butuh library su terpisah).
 */
class MainActivity : Activity() {

    private lateinit var tvState: TextView
    private lateinit var tvOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvState = findViewById(R.id.tvState)
        tvOutput = findViewById(R.id.tvOutput)

        findViewById<Button>(R.id.btnStatus).setOnClickListener {
            runRootCommand("azenith-ai status")
        }
        findViewById<Button>(R.id.btnThermal).setOnClickListener {
            runRootCommand("azenith-ai thermalkill status")
        }
        findViewById<Button>(R.id.btnRefresh).setOnClickListener {
            runRootCommand("azenith-ai status")
        }
        findViewById<Button>(R.id.btnTelegram).setOnClickListener {
            openUrl("https://t.me/yanzz091")
        }
        findViewById<Button>(R.id.btnTiktok).setOnClickListener {
            openUrl("https://tiktok.com/@azettech")
        }

        runRootCommand("azenith-ai status")
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    /** Jalankan satu perintah lewat su -c di background thread, lalu update UI. */
    private fun runRootCommand(command: String) {
        tvState.text = getString(R.string.loading)
        Thread {
            val result = execSu(command)
            runOnUiThread {
                if (result == null) {
                    tvState.text = getString(R.string.no_root)
                    tvOutput.text = ""
                    return@runOnUiThread
                }
                if (result.isBlank()) {
                    tvState.text = getString(R.string.module_missing)
                    tvOutput.text = ""
                    return@runOnUiThread
                }
                tvState.text = command
                tvOutput.text = result
            }
        }.start()
    }

    /** Eksekusi "su -c <command>", kembalikan stdout+stderr, atau null kalau root ditolak. */
    private fun execSu(command: String): String? {
        return try {
            val process = ProcessBuilder("su", "-c", command)
                .redirectErrorStream(true)
                .start()
            val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
            process.waitFor()
            output
        } catch (e: Exception) {
            null
        }
    }
}
