package com.shaneroach.taskmaster.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.shaneroach.taskmaster.dao.TaskDao;
import com.shaneroach.taskmaster.model.Task;

@Database(entities = {Task.class}, version = 3)
@TypeConverters({TaskMasterConverters.class})
public abstract class TaskMasterDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

}
