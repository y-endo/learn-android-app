package jp.yukiendo.databasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /**
     * 選択されたカクテルの主キーIDを表すフィールド
     */
    private var _cocktailId = -1

    /**
     * 選択されたカクテル名を表すフィールド
     */
    private var _cocktailName = ""

    // データベースヘルパーオブジェクト
    private val _helper = DatabaseHelper(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // カクテルリスト用ListView(lvCocktail)を取得
        val lvCocktail = findViewById<ListView>(R.id.lvCocktail)
        // lvCocktailにリスナを登録
        lvCocktail.onItemClickListener = ListItemClickListener()
    }

    override fun onDestroy() {
        // ヘルパーオブジェクトの開放
        _helper.close()
        super.onDestroy()
    }

    /**
     * 保存ボタンがタップされた時の処理メソッド
     */
    fun onSaveButtonClick(view: View) {
        // 感想欄を取得
        val etNote = findViewById<EditText>(R.id.etNote)
        // 入力された感想を取得
        val note = etNote.text.toString()
        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        val db = _helper.writableDatabase
        // まず、リストで選択されたカクテルのメモデータを削除。その後インサートを行う
        // 削除用SQL
        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?"
        // SQL文字列を元にプリペアドステートメントを取得
        var stmt = db.compileStatement(sqlDelete)
        // 変数のバインド
        stmt.bindLong(1, _cocktailId.toLong())
        // 削除SQLの実行
        stmt.executeUpdateDelete()
        //インサート用SQL文字列を用意
        val sqlInsert = "INSERT INTO cocktailmemos (_id, name, note) VALUES (?, ?, ?)"
        // SQL文字列を元にプリペアドステートメントを取得
        stmt = db.compileStatement(sqlInsert)
        // 変数のバインド
        stmt.bindLong(1, _cocktailId.toLong())
        stmt.bindString(2, _cocktailName)
        stmt.bindString(3, note)
        // インサートSQLの実行
        stmt.executeInsert()
        // 感想欄の入力値を消去
        etNote.setText("")
        // カクテル名を表示するTextViewを取得
        val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
        // カクテル名を「未選択」に変更
        tvCocktailName.text = getString(R.string.tv_name);
        // 保存ボタンを取得
        val btnSave = findViewById<Button>(R.id.btnSave)
        // 保存ボタンをタップできないように変更
        btnSave.isEnabled = false
    }

    /**
     * リストがタップされたときの処理が記述されたメンバクラス
     */
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行番号をフィールドの主キーIDに代入
            _cocktailId = position
            // タップされた行のデータを取得。これがカクテル名になるので、フィールドに代入
            _cocktailName = parent.getItemAtPosition(position) as String
            // カクテル名を表示するTextViewに表示カクテル名を設定
            tvCocktailName.text = _cocktailName
            // 保存ボタンを取得
            val btnSave = findViewById<Button>(R.id.btnSave)
            // 保存ボタンをタップできるように設定
            btnSave.isEnabled = true

            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            val db = _helper.writableDatabase
            // 主キーによる検索SQL文字列の用意
            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailId}"
            // SQLの実行
            val cursor = db.rawQuery(sql, null)
            // データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意
            var note = ""
            // SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得
            while(cursor.moveToNext()) {
                // カラムのインデックス値を取得
                val idxNote = cursor.getColumnIndex("note")
                // カラムのインデックス値を元に実際のデータを取得
                note = cursor.getString(idxNote)
            }
            // 感想のEditTextの各画面部品を取得しデータベースの値を反映
            val etNote = findViewById<EditText>(R.id.etNote)
            etNote.setText(note)
        }
    }
}
