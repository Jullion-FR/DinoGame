package com.example.gamecrashtest

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CactusGroup(
    private var cactusList: List<Cactus>
) {

    fun spawn() {
        var accumulatedOffset = 0f

        cactusList.forEach { cactus ->
            cactus.spawn()
            cactus.spriteOffset = accumulatedOffset
            accumulatedOffset += cactus.size.width.dpToPx.toFloat() / 3
        }
    }

     fun startMoving(lifecycleScope: LifecycleCoroutineScope) {
        var start:Float
        var target:Float
        for (cactus: Cactus in cactusList){
            lifecycleScope.launch(Dispatchers.Main) {
                start = cactus.x + cactus.spriteOffset
                target = -Tools.screenWidth/6 + cactus.spriteOffset

                cactus.startMoving(
                    start,
                    target
                )
            }
        }
    }

    fun collisionChecker(lifecycleScope: LifecycleCoroutineScope, dinosaur: Dinosaur){
        for (cactus: Cactus in cactusList) {
            lifecycleScope.launch {
                cactus.collisionChecker(dinosaur)
            }
        }
    }
}