package com.example.memorio

import android.R.id.home
import android.animation.ArgbEvaluator
import android.graphics.Color
import android.graphics.Color.red
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorio.models.BoardSize
import com.example.memorio.models.MemoryCard
import com.example.memorio.models.MemoryGame
import com.example.memorio.utils.DEFAULT_ICONS
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.material.snackbar.Snackbar
import java.text.FieldPosition

class HardGameActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HardGameActivity"
    }

    private lateinit var mainBoard: ConstraintLayout
    private lateinit var board: RecyclerView
    private lateinit var pairsBtn: TextView
    private lateinit var movesBtn: TextView
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter

    // calling the action bar


    private var boardSize: BoardSize = BoardSize.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hard_game)

        mainBoard = findViewById(R.id.mainBoard)
        board = findViewById(R.id.board)
        pairsBtn = findViewById(R.id.pairsBtn)
        movesBtn = findViewById(R.id.movesBtn)

        boardSetUp()

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "New Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        boardSetUp()
                    })
                }else {
                    boardSetUp()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") {_, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun boardSetUp() {
        when (boardSize) {
            BoardSize.HARD -> {
                movesBtn.text = "Hard: 6 x 4"
                pairsBtn.text = "Pairs: 0 / 12"
            }
        }
        //color progress for pairs button
        pairsBtn.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })
        board.adapter = adapter
        board.setHasFixedSize(true)
        board.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateGameWithFlip (position: Int) {
        //Error handling
        if (memoryGame.haveWonGame()) {
            // Starting a new game

            if (memoryGame.haveWonGame()) {
                showAlertDialog("Do you want to start a new game?", null, View.OnClickListener {
                    boardSetUp()
                })
            } else {
                boardSetUp()
            }
        }
        if (memoryGame.isCardFacedUp(position)){
            Snackbar.make(mainBoard, "Invalid move, try again", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (memoryGame.flipCard(position)) {
            Log.i(TAG, "Found a match! Num pairs found: ${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full),
            ) as Int
            pairsBtn.setTextColor(color)
            pairsBtn.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(mainBoard, "You won, Congratulations", Snackbar.LENGTH_LONG).show()
                CommonConfetti.rainingConfetti(mainBoard, intArrayOf(Color.BLUE, Color.CYAN, Color.MAGENTA, Color.TRANSPARENT, Color.RED)).oneShot()
            }
        }
        movesBtn.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }
}