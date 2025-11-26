package com.example.p2_memoriaexterna

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etContenido: EditText
    private lateinit var tvRuta: TextView
    private lateinit var layoutArchivos: LinearLayout

    private val ARCHIVO_UNICO = "externo.txt"
    private val CARPETA_REPORTES = "MisReportes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etContenido = findViewById(R.id.etContenido)
        tvRuta = findViewById(R.id.tvRuta)
        layoutArchivos = findViewById(R.id.layoutArchivos)

        // ====== ACTIVIDAD 1 ======
        findViewById<Button>(R.id.btnGuardar).setOnClickListener { guardarArchivoUnico() }
        findViewById<Button>(R.id.btnLeer).setOnClickListener { leerArchivoUnico() }
        findViewById<Button>(R.id.btnEliminar).setOnClickListener { eliminarArchivoUnico() }

        // ====== ACTIVIDAD 2 ======
        findViewById<Button>(R.id.btnNotas).setOnClickListener { guardarArchivoMultiple("notas.txt") }
        findViewById<Button>(R.id.btnDatos).setOnClickListener { guardarArchivoMultiple("datos.txt") }
        findViewById<Button>(R.id.btnConfig).setOnClickListener { guardarArchivoMultiple("config.json") }

        // ====== ACTIVIDAD 3 ======
        findViewById<Button>(R.id.btnCrearCarpeta).setOnClickListener { crearCarpetaReportes() }
        findViewById<Button>(R.id.btnGuardarReporte).setOnClickListener { guardarArchivoEnReportes("reporte.txt") }

        // ====== ACTIVIDAD 4 ======
        findViewById<Button>(R.id.btnListar).setOnClickListener { listarArchivos() }
    }

    // ============================
    // ACTIVIDAD 1
    // ============================
    private fun guardarArchivoUnico() {
        val texto = etContenido.text.toString()
        if (texto.isEmpty()) { Toast.makeText(this,"Escribe algo",Toast.LENGTH_SHORT).show(); return }

        val archivo = File(getExternalFilesDir(null), ARCHIVO_UNICO)
        archivo.writeText(texto)
        tvRuta.text = "Guardado en:\n${archivo.absolutePath}"
        Toast.makeText(this, "Archivo guardado", Toast.LENGTH_SHORT).show()
    }

    private fun leerArchivoUnico() {
        val archivo = File(getExternalFilesDir(null), ARCHIVO_UNICO)
        if (!archivo.exists()) { Toast.makeText(this,"Archivo no existe",Toast.LENGTH_SHORT).show(); return }

        etContenido.setText(archivo.readText())
        tvRuta.text = "Leído desde:\n${archivo.absolutePath}"
    }

    private fun eliminarArchivoUnico() {
        val archivo = File(getExternalFilesDir(null), ARCHIVO_UNICO)
        if (archivo.exists()) {
            archivo.delete()
            tvRuta.text = "Archivo eliminado"
            Toast.makeText(this,"Archivo eliminado",Toast.LENGTH_SHORT).show()
        } else { Toast.makeText(this,"No existe archivo",Toast.LENGTH_SHORT).show() }
    }

    // ============================
    // ACTIVIDAD 2
    // ============================
    private fun guardarArchivoMultiple(nombre: String) {
        val texto = etContenido.text.toString()
        if (texto.isEmpty()) { Toast.makeText(this,"Escribe algo",Toast.LENGTH_SHORT).show(); return }

        val archivo = File(getExternalFilesDir(null), nombre)
        archivo.writeText(texto)
        tvRuta.text = "Guardado en:\n${archivo.absolutePath}"
        Toast.makeText(this,"$nombre guardado",Toast.LENGTH_SHORT).show()
    }

    // ============================
    // ACTIVIDAD 3
    // ============================
    private fun crearCarpetaReportes() {
        val carpeta = File(getExternalFilesDir(null), CARPETA_REPORTES)
        if (!carpeta.exists()) {
            carpeta.mkdir()
            Toast.makeText(this,"Carpeta MisReportes creada",Toast.LENGTH_SHORT).show()
        } else { Toast.makeText(this,"Carpeta ya existe",Toast.LENGTH_SHORT).show() }
    }

    private fun guardarArchivoEnReportes(nombreArchivo: String) {
        val texto = etContenido.text.toString()
        if (texto.isEmpty()) { Toast.makeText(this,"Escribe algo",Toast.LENGTH_SHORT).show(); return }

        val carpeta = File(getExternalFilesDir(null), CARPETA_REPORTES)
        if (!carpeta.exists()) carpeta.mkdir()

        val archivo = File(carpeta, nombreArchivo)
        archivo.writeText(texto)

        tvRuta.text = "Guardado en:\n${archivo.absolutePath}"
        Toast.makeText(this,"$nombreArchivo guardado en MisReportes",Toast.LENGTH_SHORT).show()
    }

    // ============================
    // ACTIVIDAD 4 - Mini Explorador
    // ============================
    private fun listarArchivos() {
        layoutArchivos.removeAllViews()
        val carpeta = getExternalFilesDir(null)
        val archivos = carpeta?.listFiles() ?: arrayOf()

        if (archivos.isEmpty()) {
            Toast.makeText(this,"No hay archivos para mostrar",Toast.LENGTH_SHORT).show()
            return
        }

        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        archivos.forEach { archivo ->
            val tv = TextView(this)
            tv.text = "${archivo.name} - ${archivo.length()} bytes - ${formato.format(Date(archivo.lastModified()))}"
            tv.setPadding(5,5,5,5)
            tv.setOnClickListener { leerArchivoClick(archivo) }
            tv.setOnLongClickListener { eliminarArchivoClick(archivo); true }
            layoutArchivos.addView(tv)
        }
    }

    private fun leerArchivoClick(archivo: File) {
        etContenido.setText(archivo.readText())
        tvRuta.text = "Leído desde:\n${archivo.absolutePath}"
        Toast.makeText(this,"Archivo cargado",Toast.LENGTH_SHORT).show()
    }

    private fun eliminarArchivoClick(archivo: File) {
        archivo.delete()
        listarArchivos()
        Toast.makeText(this,"Archivo eliminado",Toast.LENGTH_SHORT).show()
    }
}
