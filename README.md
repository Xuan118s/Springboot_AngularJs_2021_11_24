# Springboot_AngularJs
# Order.java
```js
package com.github.xuan118s.springboot_angularjs;
import java.util.Date;

public class Order {
	    public int id;
	    public String name;
	    public String messages;
	    public Date create_date; 
	    
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMessage() {
			return messages;
		}
		public void setMessage(String messages) {
			this.messages = messages;
		}
		public Date getcreate_date() {
			return create_date;
		}
		public void setcreate_date(Date create_date) {
			this.create_date = create_date;
		}
		
		@Override
		public String toString() {
			return "Customer [id=" + id + ", name=" + name + ", message=" + messages + ", createdata=" + create_date +"]";
		}
}

```
<br>1.定義一個public get/set(讀/寫) ，方便外部訪問</br>
<br>2.所需定義值有ID(身分編號)、NAME(用戶名稱)、MESSAGE(留言訊息)、CREATE_DATE(創建留言時間)</br>
<br>3. toString() 做資料庫在Java裡的列表頁印與自我檢查</br>

<hr>


# OrderDao.java
```js
package com.github.xuan118s.springboot_angularjs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Order> findAll() {
		List<Order> list = new ArrayList<>();
		String sql = "SELECT * FROM MemTable";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, new Object[] {});
		while (rows.next()) {
			Order order = new Order();
			list.add(order);
			order.id = rows.getInt("ID");
			order.name = rows.getString("NAME");
			order.messages = rows.getString("MESSAGE");
			order.create_date = rows.getDate("DATETIME");
		}
		return list;
	}


	public void insert(Order order) {
		  jdbcTemplate.update("INSERT INTO MemTable(NAME,MESSAGE, DATETIME) "+
				  "VALUES (?,?,CURRENT_TIMESTAMP)",order.name,order.messages);
		  String sql = "select * from MemTable WHERE NAME=? AND MESSAGE=? ";
		  List<Order> list = new ArrayList<>();
		  SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,order.name,order.messages);
		  while (rows.next()) {
				list.add(order);
				order.id = rows.getInt("ID");
				order.name = rows.getString("NAME");
				order.messages = rows.getString("MESSAGE");
				order.create_date = rows.getDate("DATETIME");
			}
	}

	public void update(Order order) {
		jdbcTemplate.update("UPDATE MemTable SET NAME = ? , MESSAGE = ? WHERE ID = ?",
				order.name,order.messages,order.id);
	}


	public Order get(String id) {
		Order order = null;
		String sql = "SELECT * FROM MemTable WHERE ID=?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,id);
		while (rows.next()) {
			order = new Order();
			order.id = rows.getInt("ID");
			order.name = rows.getString("NAME");
			order.messages = rows.getString("MESSAGE");
			order.create_date = rows.getDate("DATETIME");
		}
		return order;
	}
	
	public void delete(String id) {
		String sql = "DELETE FROM MemTable WHERE ID = ? ";
		jdbcTemplate.update(sql, id );
	}
}

```

>findAll()
>>1.SQL "SELECT * FROM MemTable" 查詢所有資料  
>>2.jdbcTemplate.queryForRowSet方法的調用，返回的則是SqlRowSet對象，進而給予rows變數<br>
>>3.rows.next()幾筆資料去迴圈，並且送資料到我們的order()，資料從rows讀取並給予

>insert()
>>1.SQL "INSERT INTO MemTable(NAME,MESSAGE, DATETIME) "+"VALUES (?,?,CURRENT_TIMESTAMP)" 新增一筆新的資料 *CURRENT_TIMESTAMP給予創建時間，ID則是用自動遞增的方式分配<br>
>>2.jdbcTemplate.queryForRowSet方法的調用，返回的則是SqlRowSet對象，進而給予rows變數
>>3.讀取input框裡的值進行新增資料

>update()
>>1.SQL "UPDATE MemTable SET NAME = ? , MESSAGE = ? WHERE ID = ?" 為更新資料，從每個不重複的身分編號做更改
>>2.資料從NAME與MESSAGE從input裡讀取

>get()
>>1.SQL "SELECT * FROM MemTable WHERE ID=?" 讀取相對id的資料
>>2.後續步驟同insert()的方法

>delete()
>>1.SQL "DELETE FROM MemTable WHERE ID = ?" 刪除指定ID的所有資料欄資料
<hr>

# MainContoller.java
```js
package com.github.xuan118s.springboot_angularjs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@Autowired
	private OrderDao orderDao;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(@RequestBody Order order) {
		Map<String, Object> result = new HashMap<>();

		orderDao.update(order);
		
		result.put("id", order.id);
		return result;
	}
	
	@PostMapping("/newdata")
	public @ResponseBody Map<String, Object> newdata(@RequestBody Order order) {
		Map<String, Object> result = new HashMap<>();
	
		orderDao.insert(order);
		
		result.put("id", order.id);
		return result;
	}
	
	@PostMapping("/get")
	public @ResponseBody Object get(String id) {
		return orderDao.get(id);
	}

	@PostMapping("/findAll")
	public @ResponseBody Object findAll() {
		return orderDao.findAll();
	}

	@PostMapping("/delete")
	public @ResponseBody Map<String, Object> delete(String id) {
		Map<String, Object> result = new HashMap<>();
		orderDao.delete(id);
		result.put("id", id);
		return result;
	}
}

 ```
 
<h1>MainController的Springframework.web介紹:</h1>
<br>1.@Controller 最後會回到我們的Web Page頁面</br>
<br>2.@Autowired 可以對class成員變量、方法及構造函數進行標註，完成自動裝配的工作</br>
<br>3.@GetMapping 可在method層面上用作處理 http 的請求</br>
<br>4.@PostMapping 使用RequestMethod.POST進行註釋</br>
<br>5.@Responsebody 註解表示該方法的返回的結果直接寫入 HTTP 響應正文（ResponseBody）中，一般在非同步獲取資料時使用</br>
<br>6.@RequestBody 註解則是將 HTTP 請求正文插入方法中，使用適合的 HttpMessageConverter 將請求體寫入某個物件</br>
<hr>
<h3>MainController介紹:</h3>
>@PostMapping("/save")
>>讀取一整個Map從Html上，並且執行OrderDao.update，去做資料庫裡的資料更新

>@PostMapping("/newdata")
>>讀取一整個Map從Html上，並且執行orderDao.insert，去做資料庫的資料新增

>@PostMapping("/get")
>>給予指定ID的Map從資料庫，並且執行orderDao.get，回傳給Html

>@PostMapping("/findAll")
>>給予全部的Map從資料庫，並且執行orderDao.findAll，回傳給Html

>@PostMapping("/delete")
>>讀取單一ID從HTML，並且執行orderDao.delete，刪除指定的資料庫資料

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="//cdn.bootcss.com/angular.js/1.5.6/angular.min.js"></script>
<script type="text/javascript">
	/*<![CDATA[*/
	var app = angular.module('app', []);
	app.controller('MainController', function($rootScope, $scope, $http) {

		$scope.data = {};
		$scope.rows = [];
		
		//新增(更改)
		$scope.newdata = function() {
			
			$http({
				url : '/newdata',
				method : 'POST',
				data : $scope.data
			}).success(function(r) {
				//保存成功後更新資訊
				$scope.get(r.id);
			});
		}
	

		//編輯(完成)
		$scope.edit = function(id) {
			$scope.data = {
					id : id.id,
					name : id.name,
					message : id.message,
					date : id.create_date,
				};
		}

		//移除(完成)
		$scope.remove = function(id) {
			for ( var i in $scope.rows) {
				var row = $scope.rows[i];
				if (id == row.id) {
					$scope.rows.splice(i, 1);
					return;
				}
			}
		}
		
		//保存(完成)
		$scope.save = function() {
			$http({
				url : '/save',
				method : 'POST',
				data : $scope.data
			}).success(function(r) {
				//保存成功後更新資訊
				$scope.get(r.id);
			});
		}

		//刪除(完成)
		$scope.del = function(id) {
			$http({
				url : '/delete?id=' + id,
				method : 'POST',
			}).success(function(r) {
				//刪除移除數據
				$scope.remove(r.id);
			});
		}

		//收取數據(完成)
		$scope.get = function(id) {
			$http({
				url : '/get?id=' + id,
				method : 'POST',
			}).success(function(data) {
				for ( var i in $scope.rows) {
					var row = $scope.rows[i];
					if (data.id == row.id) {
						row.id = data.id;
						row.name = data.name;
						row.message = data.message;
						row.data = data.date;
						return;
					}
				}
				$scope.rows.push(data);
			});
		}

		//初始化載入資料(完成)
		$http({
			url : '/findAll',
			method : 'POST'
		}).success(function(rows) {
			for ( var i in rows) {
				var row = rows[i];
				$scope.rows.push(row);
			}
		});
	});
	/*]]>*/
</script>
</head>
<body ng-app="app" ng-controller="MainController">
	<h1>留言板Web (測試)</h1>

	<br />
	<br />
	<h3>編輯訊息：</h3>
	<input id="id" type="hidden" ng-model="data.id" />
	<table cellspacing="2" style="background-color: #a0c6e5">
		<tr>
			<td>ID：</td>
			<td id="id" ng-model="data.id"> {{ data.id }}</td>
			<td>名稱：</td>
			<td><input id="name" ng-model="data.name"/></td>
			<td>留言：</td>
			<td><input id="message" ng-model="data.message"/></td>
			<td>日期：</td>
			<td id="date" ng-model="data.date"> {{ data.date }}</td>
		
		
		</tr>
	</table>
	<input type="button" value="新增" ng-click="newdata()" />
	<input type="button" value="(修改資料)保存" ng-click="save()" />
	<br />
	<h3>留言板列表：</h3>
	<table cellspacing="1" style="background-color: #a0c6e5">
		<tr
			style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
		
			<td>ID</td>
			<td>名稱</td>
			<td>留言</td>
			<td>留言時間</td>
			<td>編輯</td>
		</tr>
		<tr ng-repeat="row in rows" bgcolor='#F4FAFF'>
			
			<td>{{row.id}}</td>
			<td>{{row.name}}</td>
			<td>{{row.message}}</td>
			<td>{{row.create_date}}</td>
			
			<td><input ng-click="edit(row)" value="编辑" type="button" /><input
				ng-click="del(row.id)" value="删除" type="button" /></td>
		</tr>
	</table>

	<br />
</body>
</html>
```
