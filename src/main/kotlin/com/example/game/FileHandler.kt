package com.mygdx.game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException

class FileHandler {
    companion object{

        fun getFileJson(fileName: String): String{
            val file = File(fileName)
            if (!file.exists()) {
                throw java.io.FileNotFoundException("File not found: $fileName")
            }
            return file.readText()
        }
    }
}
