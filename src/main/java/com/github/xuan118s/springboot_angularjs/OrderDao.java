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
//		System.out.print(order.getId());
//		System.out.print(order.create_date);
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
		jdbcTemplate.update(sql,
				 id );
	}
}
