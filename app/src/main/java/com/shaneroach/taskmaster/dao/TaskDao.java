package com.shaneroach.taskmaster.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shaneroach.taskmaster.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insertATask(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();

    @Delete
    public void deleteAll(List<Task> taskList);
}
