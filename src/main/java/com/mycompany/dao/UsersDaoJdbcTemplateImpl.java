/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.mycompany.models.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;
/**
 *
 * @author user
 */
public class UsersDaoJdbcTemplateImpl implements UsersDao {
    private JdbcTemplate template;

    private final String SQL_SELECT_ALL = 
            "SELECT * FROM fix_user";
    private Map<Integer, User> usersMap = new HashMap<>();
    
    private final String SQL_SELECT_USER = 
            "SELECT * FROM fix_user WHERE fix_user.id = ?";
    private final String SQL_SELECT_ALL_BY_FIRST_NAME = 
            "SELECT * FROM fix_user WHERE first_name = ?";
    
    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }
    
    private RowMapper<User> userRowMapper
            = (ResultSet resultSet, int i) -> {
        Integer id = resultSet.getInt("id");
                
        if (!usersMap.containsKey(id)) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            User user = new User(id, firstName, lastName);
            usersMap.put(id , user);
        }
        return usersMap.get(id);
    };
    
    @Override
    public List<User> findAllByFirstName(String firstName) {
       return template.query(SQL_SELECT_ALL_BY_FIRST_NAME, userRowMapper, firstName); 
    }
    @Override
    public Optional<User> find(Integer id) {
        template.query(SQL_SELECT_USER, userRowMapper, id);
        
        if (usersMap.containsKey(id)) {
            return Optional.of(usersMap.get(id));
        }
        return Optional.empty();
    }
    @Override
    public void save(User model) {
    }
    @Override
    public void update(User model) {
    }
    @Override
    public void delete(Integer id) {
    }
    @Override
    public List<User> findAll() {
        return template.query(SQL_SELECT_ALL, userRowMapper);
    }
}
