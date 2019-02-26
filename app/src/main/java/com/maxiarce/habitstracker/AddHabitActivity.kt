package com.maxiarce.habitstracker

import android.Manifest
import android.animation.Animator
import android.app.Activity
import android.content.Context
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
import android.support.v7.app.AlertDialog


class AddHabitActivity : AppCompatActivity() {
    lateinit var habitText : EditText
    lateinit var habitDescription : EditText
    lateinit var radioGroup: RadioGroup
    var typeCalendar:Int = 30
    var STORAGE_PERMISSION_CODE = 1;
    var pictureloaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        intent = Intent(this,HabitsDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        habitText = findViewById(R.id.editText_habit_name)
        habitDescription = findViewById(R.id.editText_habit_description)
        radioGroup = findViewById(R.id.radio_group)


        floatingActionButton_save.setOnClickListener{
            if(pictureloaded){
                revealMainActivity()
            }else{
                Toast.makeText(this,"Select a picture",Toast.LENGTH_LONG).show()
            }
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
                    radio_group.visibility = View.GONE
                    textView_type.visibility = View.GONE
                    spinner.visibility = View.GONE
                    textView_setReward.visibility = View.GONE
                    floatingActionButton_save.hide()
                    button_pick_picture.visibility = View.GONE

                }
                else{
                    editText_habit_description.visibility = View.VISIBLE
                    textView_pickcolor.visibility = View.VISIBLE
                    radio_group.visibility = View.VISIBLE
                    textView_type.visibility = View.VISIBLE
                    spinner.visibility = View.VISIBLE
                    textView_setReward.visibility = View.VISIBLE
                    floatingActionButton_save.show()
                    button_pick_picture.visibility = View.VISIBLE
                }
            }

        })

    }


    fun revealMainActivity(){
        if (editText_habit_name.text.isNotBlank()){


            //save to sqlite
            var habitItem = HabitItem(
                habitText.text.toString(),
                habitDescription.text.toString(),
                getColorPick(radioGroup),
                0,typeCalendar)

            var db = DatabaseHelper(this)
            db.insertData(habitItem)


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
        }else{
        Toast.makeText(this, "Something is Empty", Toast.LENGTH_LONG).show()
        }

    }

    fun getColorPick(radioGroup: RadioGroup): String{
        return  when(radioGroup.checkedRadioButtonId){
            radio1.id -> "#E53935"
            radio2.id -> "#D81B60"
            radio3.id -> "#8E24AA"
            radio4.id -> "#1E88E5"
            radio5.id -> "#00897B"
            radio6.id -> "#7CB342"
            radio7.id -> "#FDD835"
            radio8.id -> "#F4511E"
            else -> "#E53935"
            }
        }

    fun selectPhoto(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
            val intentOnResult = Intent(Intent.ACTION_PICK)
            intentOnResult.type = "image/*"
            startActivityForResult(intentOnResult,0)
        }else{
            requestStoragePermissions()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val f = File(this.filesDir.path,habitText.text.toString())
            if (!f.exists()) {
                try {
                    f.createNewFile()
                    copyFile(File(getRealPathFromURI(data.data!!)), f)
                    pictureloaded = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    pictureloaded = false
                }

            }
        }
    }

    fun getRealPathFromURI(contentUri: Uri): String {

        val proj = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null
        source = FileInputStream(sourceFile).channel
        destination = FileOutputStream(destFile).channel
        if (destination != null && source != null) {
            destination!!.transferFrom(source, 0, source!!.size())
        }
        if (source != null) {
            source!!.close()
        }
        if (destination != null) {
            destination!!.close()
        }


    }

    fun requestStoragePermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because we need to read the picture that you will use as reward")
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
                    )
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .create().show()

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
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




}


