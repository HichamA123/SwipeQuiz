package com.example.swipequiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val questions = arrayListOf<Question>()
    private val questionAdapter = QuestionAdapter(questions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        questions.addAll(listOf(
            Question(getString(R.string.question1), false), Question(getString(R.string.question2), true),
            Question(getString(R.string.question3), true), Question(getString(R.string.question4), true)
        ))

        //because i initialized questions (line 22)
        questionAdapter.notifyDataSetChanged()

        rvQuestions.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvQuestions.adapter = questionAdapter

        rvQuestions.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(rvQuestions)
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            //not being used
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val question = questions.get(position)
                checkDirectionAndAnswer(question, direction)
                questionAdapter.notifyItemChanged(viewHolder.adapterPosition)

            }
        }

        return ItemTouchHelper(callback)

    }

    /**
     * Gives feedback to the user based on his answer
     */
    private fun checkDirectionAndAnswer(question: Question, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            //questions with answer: false
            if (!question.answer) correctSnackbar(question.answer)
            else incorrectSnackBar(question.answer)

        //swipe right
        } else {
            //questions with answer: true
            if (question.answer) correctSnackbar(question.answer)
            else incorrectSnackBar(question.answer)
        }

    }

    private fun incorrectSnackBar(answer: Boolean) {
        Snackbar.make(rvQuestions, getString(R.string.fb_incorrect) + " " + answer, Snackbar.LENGTH_SHORT).show()
    }

    private fun correctSnackbar(answer: Boolean) {
        Snackbar.make(rvQuestions, getString(R.string.fb_correct) + " " +  answer, Snackbar.LENGTH_SHORT).show()
    }





}
