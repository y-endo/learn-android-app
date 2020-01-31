package jp.yukiendo.intentsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 画面部品ListViewを取得
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // SimpleAdapterで仕様するMutableListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
        // Mapオブジェクトの用意とmenuListへのデータ登録
        var menu = mutableMapOf("name" to "ハンバーグ定食", "price" to "800円")
        menuList.add(menu)
        menu = mutableMapOf("name" to "からあげ定食", "price" to "800円")
        menuList.add(menu)
        menu = mutableMapOf("name" to "野菜炒め定食", "price" to "800円")
        menuList.add(menu)

        // SimpleAdapter第4引数from用データの用意
        val from = arrayOf("name", "price")
        // SimpleAdapter第5引数to用データの用意
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        // SimpleAdapterを生成
        val adapter = SimpleAdapter(applicationContext, menuList, android.R.layout.simple_list_item_2, from, to)
        // アダプタの登録
        lvMenu.adapter = adapter

        // リストタップのリスナクラス登録
        lvMenu.onItemClickListener = ListItemClickListener()
    }

    // リストがタップされた時の処理が記述されたメンバクラス
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行のデータを取得。SimpleAdapterでは1行分のデータはMutableMap型
            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            // 定食名と金額を取得
            val menuName = item["name"]
            val menuPrice = item["price"]
            // インテントオブジェクトを生成
            val intent = Intent(applicationContext, MenuThanksActivity::class.java)
            // 第2画面に送るデータを格納
            intent.putExtra("menuName", menuName)
            intent.putExtra("menuPrice", menuPrice)
            // 第2画面の起動
            startActivity(intent)
        }
    }
}
