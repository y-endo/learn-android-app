package jp.yukiendo.menusample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    // リストビューに表示するリストデータ
    private var _menuList: MutableList<MutableMap<String, Any>>? = null
    // SimpleAdapterの第4引数fromに使用する定数フィールド
    private val FROM = arrayOf("name", "price")
    // SimpleAdapterの第5引数toに使用する定数フィールド
    private val TO = intArrayOf(R.id.tvMenuName, R.id.tvMenuPrice)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 定食メニューListオブジェクトをprivateメソッドを利用して用意し、フィールドに格納
        _menuList = createTeishokuList()
        // 画面部品ListViewを取得
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // SimpleAdapterを生成
        val adapter = SimpleAdapter(applicationContext, _menuList, R.layout.row, FROM, TO)
        // アダプタの登録
        lvMenu.adapter = adapter
        // リストタップのリスナクラス登録
        lvMenu.onItemClickListener = ListItemClickListener()

        registerForContextMenu(lvMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // オプションメニュー用xmlファイルをインフレイト
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        // 親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreateContextMenu(menu: ContextMenu, view: View, menuInfo: ContextMenu.ContextMenuInfo) {
        // 親クラスの同名メソッドの呼び出し
        super.onCreateContextMenu(menu, view, menuInfo)
        // コンテキストメニュー用xmlファイルをインフレイト
        menuInflater.inflate(R.menu.menu_context_menu_list, menu);
        // コンテキストメニューのヘッダタイトルを設定
        menu.setHeaderTitle(R.string.menu_list_context_header)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 選択されたメニューのIDのR値による処理の分岐
        when(item.itemId) {
            // 定食メニューが選択された場合の処理
            R.id.menuListOptionTeishoku ->
                // 定食メニューリストデータの生成
                _menuList = createTeishokuList()
            // カレーメニューが選択された場合の処理
            R.id.menuListOptionCurry ->
                // カレーメニューリストデータの生成
                _menuList = createCurryList()
        }
        // 画面部品ListViewを取得
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // SimpleAdapterを生成
        val adapter = SimpleAdapter(applicationContext, _menuList, R.layout.row, FROM, TO)
        // アダプタの登録
        lvMenu.adapter = adapter
        // 親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // 長押しされたビューに関する情報が格納されたオブジェクトを取得
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        // 長押しされたリストのポジションを取得
        val listPosition = info.position
        // ポジションから長押しされたメニュー情報Mapオブジェクトを取得
        val menu = _menuList!![listPosition]

        // 選択されたメニューのIDのR値による処理の分岐
        when(item.itemId) {
            // [説明を表示]メニューが選択された時の処理
            R.id.menuListContextDesc -> {
                // メニューの説明文字列を取得
                val desc = menu["desc"] as String
                // トーストを表示
                Toast.makeText(applicationContext, desc, Toast.LENGTH_LONG).show()
            }
            // [ご注文]メニューが選択された時の処理
            R.id.menuListContextOrder ->
                // 注文処理
                order(menu)
        }

        // 親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onContextItemSelected(item)
    }

    private fun createTeishokuList(): MutableList<MutableMap<String, Any>> {
        // 定食メニューリスト用のListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        // 「から揚げ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        var menu = mutableMapOf("name" to "から揚げ定食", "price" to 800, "desc" to "若鶏のから揚げにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menu)
        // 「ハンバーグ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        menu = mutableMapOf("name" to "ハンバーグ定食", "price" to 850, "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menu)

        return menuList
    }

    private fun createCurryList(): MutableList<MutableMap<String, Any>> {
        // カレーメニューリスト用のListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        // 「ビーフカレー」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        var menu = mutableMapOf("name" to "ビーフカレー", "price" to 520, "desc" to "特選スパイスをきかせた国産ビーフ100%のカレーです。")
        menuList.add(menu)
        // 「ポークカレー」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        menu = mutableMapOf("name" to "ポークカレー", "price" to 420, "desc" to "特選スパイスをきかせた国産ポーク100%のカレーです。")
        menuList.add(menu)

        return menuList
    }

    private fun order(menu: MutableMap<String, Any>) {
        // 定食名と金額を取得。Mapの値部分がAny型なのでキャストが必要
        val menuName = menu["name"] as String
        val menuPrice = menu["price"] as Int

        // インテントオブジェクトを生成
        val intent = Intent(applicationContext, MenuThanksActivity::class.java)
        // 第2画面に送るデータを格納
        intent.putExtra("menuName", menuName)
        // MenuTanksActivityでのデータ受け取りと合わせるために、金額にここで「円」を追加する
        intent.putExtra("menuPrice", "${menuPrice}円")
        // 第2画面の起動
        startActivity(intent)
    }

    // リストがタップされた時の処理が記述されたメンバクラス
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行のデータを取得。SimpleAdapterでは1行分のデータはMutableMap型
            val item = parent.getItemAtPosition(position) as MutableMap<String, Any>
            // 注文処理
            order(item)
//            // 定食名と金額を取得
//            val menuName = item["name"] as String
//            val menuPrice = item["price"] as Int
//            // インテントオブジェクトを生成
//            val intent = Intent(applicationContext, MenuThanksActivity::class.java)
//            // 第2画面に送るデータを格納
//            intent.putExtra("menuName", menuName)
//            intent.putExtra("menuPrice", "${menuPrice}円")
//            // 第2画面の起動
//            startActivity(intent)
        }
    }
}
