package com.maxiarce.habitstracker.activities

import android.Manifest
import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.*
import com.maxiarce.habitstracker.models.HabitItem
import kotlinx.android.synthetic.main.activity_add_habit.*
import java.io.*
import java.nio.channels.FileChannel
import android.support.v4.content.ContextCompat
import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.app.AlertDialog
import com.maxiarce.habitstracker.helpers.DatabaseHelper
import com.maxiarce.habitstracker.R
import java.util.*


class AddHabitActivity : AppCompatActivity() {
    lateinit var habitText : EditText
    lateinit var habitDescription : EditText
    lateinit var radioGroup: RadioGroup
    lateinit var colorPick: String
    lateinit var pictureUri: Uri
    var typeCalendar:Int = 30
    private var STORAGE_PERMISSION_CODE = 1;
    private var pictureloaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        intent = Intent(this, HabitsDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        habitText = findViewById(R.id.editText_habit_name)
        habitDescription = findViewById(R.id.editText_habit_description)
        radioGroup = findViewById(R.id.radioGroup_pick_color_edit_activity)
        colorPick = ""


        floatingActionButton_save.setOnClickListener{
            if(editText_habit_name.text.isNotBlank() && pictureloaded && colorPick.isNotBlank() ){
                saveToDatabase()
                revealMainActivity()
            }else{
                if(colorPick.isBlank()){
                    Toast.makeText(this,"Choose a color habit",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Select a picture",Toast.LENGTH_LONG).show()
                }
            }

        }



        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                radio1.id -> colorPick = "#e57373"
                radio2.id -> colorPick = "#9575CD"
                radio3.id -> colorPick = "#4FC3F7"
                radio4.id -> colorPick = "#81C784"
                radio5.id -> colorPick = "#DCE775"
                radio6.id -> colorPick = "#FFF176"
                radio7.id -> colorPick = "#FFB74D"
                radio8.id -> colorPick = "#A1887F"
                else -> colorPick = ""
            }
            ConstrainLayout_addHabitActivity.setBackgroundColor(Color.parseColor(colorPick))
        }


        button_pick_picture.setOnClickListener{
            selectPhoto()
        }

        val typeSpinner = findViewById<Spinner>(R.id.spinner)
        val typeString =  arrayOf("30","60")
        spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeString)
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Please select how many days you want to keep this habit",Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeCalendar = when(position){
                    0 -> 30
                    1 -> 60
                    else -> 30
                }

            }

        }


        habitText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    editText_habit_description.visibility = View.GONE
                    textView_pickcolor.visibility = View.GONE
                    radioGroup_pick_color_edit_activity.visibility = View.GONE
                    textView_type.visibility = View.GONE
                    spinner.visibility = View.GONE
                    textView_setReward.visibility = View.GONE
                    floatingActionButton_save.hide()
                    button_pick_picture.visibility = View.GONE

                }
                else{
                    editText_habit_description.visibility = View.VISIBLE
                    textView_pickcolor.visibility = View.VISIBLE
                    radioGroup_pick_color_edit_activity.visibility = View.VISIBLE
                    textView_type.visibility = View.VISIBLE
                    spinner.visibility = View.VISIBLE
                    textView_setReward.visibility = View.VISIBLE
                    floatingActionButton_save.show()
                    button_pick_picture.visibility = View.VISIBLE
                }
            }

        })

    }

    fun saveToDatabase(){

        copyImage(pictureUri)

        val listOfRandomDays = generateRandomDays(typeCalendar)

        //save to sqlite
        val habitItem = HabitItem(
            habitText.text.toString(),
            habitDescription.text.toString(),
            colorPick,
            0,listOfRandomDays,typeCalendar)

        val db = DatabaseHelper(this)
        db.insertData(habitItem)


    }

    fun revealMainActivity(){



            val locations = IntArray(2)
            floatingActionButton_save.getLocationOnScreen(locations)

            val x = locations[0] + (floatingActionButton_save.width/2)
            val y = locations[1] + (floatingActionButton_save.height/2)


            val startRadius = Math.max(ConstrainLayout_addHabitActivity.width.toDouble(),ConstrainLayout_addHabitActivity.height.toDouble())
            val endingRadius = 0

            val  anim = ViewAnimationUtils.createCircularReveal(ConstrainLayout_addHabitActivity,x,y,startRadius.toFloat(),endingRadius.toFloat())

            anim.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    startActivity(intent)
                }

            })
            anim.start()

        }


    fun selectPhoto(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
            val intentOnResult = Intent(Intent.ACTION_PICK)
            intentOnResult.type = "image/*"
            intentOnResult.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            startActivityForResult(intentOnResult,0)
        }else{
            requestStoragePermissions()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?){
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && intent != null) {
            pictureUri = intent.data!!
            pictureloaded = true
        }

    }

    fun getRealPathFromURI(contentUri: Uri): String {

        val proj = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(contentUri,proj,null,null,null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val str = cursor.getString(columnIndex)
        cursor.close()
        return str
    }

    fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }

        val source: FileChannel? = FileInputStream(sourceFile).channel
        val destination: FileChannel? = FileOutputStream(destFile).channel

        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        if (source != null) {
            source.close()
        }
        if (destination != null) {
            destination.close()
        }


    }

    fun copyImage(uri: Uri){

        val fileDestination = File(this.filesDir.path,habitText.text.toString())
        val fileSource = File(getRealPathFromURI(uri))
        fileDestination.createNewFile()
        copyFile(fileSource, fileDestination)
        try {
            contentResolver.delete(uri,null,null)
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(this,"Due to limitations you must delete manually the picture from your gallery",Toast.LENGTH_SHORT).show()
        }
    }

    fun requestStoragePermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because we need to read the picture that you will use as reward")
                .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
                    )
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .create().show()

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectPhoto()
            }else{
                requestStoragePermissions()
            }

        }
    }

    // generates numbers wich will be used to reveal the picture
    fun generateRandomDays(typeCalendar: Int):String {
        val j = Random()
        val list = ArrayList<String>()
        var number: String
        for (counter in 1..typeCalendar) {
            number = j.nextInt(typeCalendar+1).toString()
            while (list.contains(number) || number == "0") {
                number = j.nextInt(typeCalendar+1).toString()
            }
            list.add(number)
        }

        // convert array to string
        val strSeparator = ","
        var str = ""
        for ( i in list.indices) {
            str = str + list[i]
            if (i < list.size - 1) {
                str = str + strSeparator
            }
        }
        return str
    }


}


