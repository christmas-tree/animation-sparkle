package nl.pvanassen.christmas.tree.animation.sparkle.animation

import nl.pvanassen.christmas.tree.animation.common.model.Animation
import nl.pvanassen.christmas.tree.animation.common.model.TreeModel
import nl.pvanassen.christmas.tree.animation.common.util.ColorUtils
import nl.pvanassen.christmas.tree.canvas.Canvas
import java.util.*
import javax.inject.Singleton

@Singleton
class Sparkle(private val canvas: Canvas, private val treeModel: TreeModel): Animation {

    private val sparkles = Array(treeModel.strips) { IntArray(treeModel.ledsPerStrip) }

    private val direction = Array(treeModel.strips) { BooleanArray(treeModel.ledsPerStrip) }

    private var cnt = 0.0

    private val steps = arrayOf(255, 210, 180, 140, 110, 80, 40)

    private val random = Random()

    override fun getFrame(seed: Long, frame: Int, nsPerFrame: Int): ByteArray {
        val color = this.color
        (0 until treeModel.strips).forEach {x ->
             (0 until treeModel.ledsPerStrip).forEach outer@{ y ->
                val step = steps.indexOf(sparkles[x][y])
                if (step == -1) {
                    return@outer
                }
                if (step == steps.size - 1 && direction[x][y]) {
                    direction[x][y] = false
                }
                val brightness = when {
                    step == 0 -> 0
                    direction[x][y] -> steps[step + 1]
                    else -> steps[step - 1]
                }
                sparkles[x][y] = brightness
                canvas.setValue(x, y, ColorUtils.fadeColor(color, brightness))
            }
        }

        (0 until treeModel.strips).forEach {idx ->
            val y = random.nextInt(treeModel.ledsPerStrip)
            sparkles[idx][y] = steps[steps.size - 1]
            direction[idx][y] = true
        }
        return canvas.getValues()
    }

    private val color: Int
        get() {
            cnt += Math.random() / 1000.0
            if (cnt > 1) {
                cnt--
            }
            return ColorUtils.makeColorHSB(cnt.toFloat(), 1f, 1f)
        }
}
