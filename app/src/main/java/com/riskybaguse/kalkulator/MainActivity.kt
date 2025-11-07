package com.riskybaguse.kalkulator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riskybaguse.kalkulator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

// Kelas utama dari aplikasi kalkulator
// Kelas ini mewarisi (extends) AppCompatActivity agar bisa memakai fitur Activity Android
class MainActivity : AppCompatActivity() {

    // ViewBinding digunakan untuk menghubungkan kode Kotlin dengan komponen XML
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan layout activity_main.xml dengan kode menggunakan ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view) // Menampilkan layout di layar

        // Tombol untuk mengaktifkan atau menonaktifkan mode gelap
        binding.switchDarkMode.setOnClickListener {
            if (binding.switchDarkMode.isChecked) {
                // Jika tombol switch aktif, ubah tampilan ke mode gelap
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Jika tidak aktif, kembalikan ke mode terang
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Fungsi untuk menambahkan karakter (angka/operator) ke tampilan kalkulator
        fun appendOnExpressions(string: String, canClear: Boolean) {
            // Jika sudah ada hasil sebelumnya, kosongkan tampilan ekspresi
            if (binding.tvResult.text.isNotEmpty()) {
                binding.tvExpressions.text = ""
            }

            // Jika canClear true, berarti kita mengetik angka baru (bukan hasil operasi)
            if (canClear) {
                binding.tvResult.text = "" // Kosongkan hasil
                binding.tvExpressions.append(string) // Tambahkan angka/operator ke layar
            } else {
                // Jika canClear false, berarti kita menambahkan operator setelah hasil
                binding.tvExpressions.append(binding.tvResult.text)
                binding.tvExpressions.append(string)
                binding.tvResult.text = ""
            }
        }

        // ==== BAGIAN NOMOR ====
        // Setiap tombol angka dihubungkan ke fungsi appendOnExpressions
        binding.text1.setOnClickListener { appendOnExpressions("1", true) }
        binding.text2.setOnClickListener { appendOnExpressions("2", true) }
        binding.text3.setOnClickListener { appendOnExpressions("3", true) }
        binding.text4.setOnClickListener { appendOnExpressions("4", true) }
        binding.text5.setOnClickListener { appendOnExpressions("5", true) }
        binding.text6.setOnClickListener { appendOnExpressions("6", true) }
        binding.text7.setOnClickListener { appendOnExpressions("7", true) }
        binding.text8.setOnClickListener { appendOnExpressions("8", true) }
        binding.text9.setOnClickListener { appendOnExpressions("9", true) }
        binding.text0.setOnClickListener { appendOnExpressions("0", true) }
        binding.textKoma.setOnClickListener { appendOnExpressions(".", true) }

        // ==== BAGIAN OPERATOR ====
        // Setiap operator juga ditambahkan dengan canClear = false (karena menyambung ekspresi)
        binding.textPlus.setOnClickListener { appendOnExpressions(" + ", false) }
        binding.textMin.setOnClickListener { appendOnExpressions(" - ", false) }
        binding.textKali.setOnClickListener { appendOnExpressions(" * ", false) }
        binding.textBagi.setOnClickListener { appendOnExpressions(" / ", false) }
        binding.textOpen.setOnClickListener { appendOnExpressions("(", false) }
        binding.textClose.setOnClickListener { appendOnExpressions(")", false) }

        // ==== TOMBOL CLEAR ====
        // Menghapus seluruh teks di layar (ekspresi dan hasil)
        binding.textClear.setOnClickListener {
            binding.tvExpressions.text = ""
            binding.tvResult.text = ""
        }

        // ==== TOMBOL BACKSPACE ====
        // Menghapus satu karakter terakhir dari ekspresi
        binding.backSpaceButton.setOnClickListener {
            val string = binding.tvExpressions.text.toString()
            if (string.isNotEmpty()) {
                binding.tvExpressions.text = string.substring(0, string.length - 1)
            }
            binding.tvResult.text = ""
        }

        // ==== TOMBOL SAMA DENGAN ====
        // Menghitung hasil dari ekspresi matematika yang dimasukkan
        binding.textEquals.setOnClickListener {
            try {
                // ExpressionBuilder digunakan untuk membaca ekspresi matematika (dari library exp4j)
                val expression = ExpressionBuilder(binding.tvExpressions.text.toString()).build()
                val result = expression.evaluate() // Evaluasi hasilnya
                val longResult = result.toLong()

                // Jika hasilnya bilangan bulat (misal 5.0), tampilkan tanpa koma
                if (result == longResult.toDouble())
                    binding.tvResult.text = longResult.toString()
                else
                // Jika hasilnya desimal, tampilkan apa adanya
                    binding.tvResult.text = result.toString()

            } catch (e: Exception) {
                // Jika terjadi error (misalnya input salah), tampilkan di Logcat
                Log.d("Exception", " message : " + e.message)
            }
        }
    }
}