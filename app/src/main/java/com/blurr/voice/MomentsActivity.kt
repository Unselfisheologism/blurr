package com.blurr.voice

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blurr.voice.data.TaskHistoryItem
import com.blurr.voice.utilities.Logger
import com.blurr.voice.managers.PuterManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.future.await

class MomentsActivity : BaseNavigationActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var adapter: MomentsAdapter
    private lateinit var puterManager: PuterManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moments_content)
        
        // Initialize puter manager
        puterManager = PuterManager.getInstance(this)
        
        // Setup back button
        findViewById<TextView>(R.id.back_button).setOnClickListener {
            finish()
        }
        
        // Initialize views
        recyclerView = findViewById(R.id.task_history_recycler_view)
        emptyState = findViewById(R.id.empty_state)
        
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MomentsAdapter(emptyList())
        recyclerView.adapter = adapter
        
        // Load task history
        loadTaskHistory()
    }
    
    private fun loadTaskHistory() {
        if (!puterManager.isUserSignedIn()) {
            showEmptyState()
            return
        }
        
        lifecycleScope.launch {
            try {
                // Get task history from puter.js key-value store - use a method that exists
                val taskHistoryFuture = puterManager.getTaskHistoryFromKvStore()
                val taskHistory: List<TaskHistoryItem> = taskHistoryFuture.await()
                
                if (taskHistory.isNotEmpty()) {
                    // Sort by startedAt in descending order (most recent first)
                    val sortedTaskHistory = taskHistory.sortedByDescending { taskItem: TaskHistoryItem ->
                        taskItem.startedAt ?: 0
                    }
                    
                    if (sortedTaskHistory.isNotEmpty()) {
                        showTaskHistory(sortedTaskHistory)
                    } else {
                        showEmptyState()
                    }
                } else {
                    showEmptyState()
                }
            } catch (e: Exception) {
                Logger.e("MomentsActivity", "Error loading task history", e)
                showEmptyState()
            }
        }
    }
    
    private fun showTaskHistory(taskHistory: List<TaskHistoryItem>) {
        adapter = MomentsAdapter(taskHistory)
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
    }
    
    private fun showEmptyState() {
        recyclerView.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
    }
    
    override fun getContentLayoutId(): Int = R.layout.activity_moments_content
    
    override fun getCurrentNavItem(): BaseNavigationActivity.NavItem = BaseNavigationActivity.NavItem.MOMENTS
}