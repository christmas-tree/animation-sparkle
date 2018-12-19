package nl.pvanassen.christmas.tree.animation.sparkle.animation

import nl.pvanassen.christmas.tree.animation.common.model.Animation
import nl.pvanassen.christmas.tree.animation.common.model.TreeModel
import nl.pvanassen.christmas.tree.animation.common.util.ColorUtils
import nl.pvanassen.christmas.tree.animation.common.util.CommonUtils
import nl.pvanassen.christmas.tree.canvas.Canvas
import javax.inject.Singleton

@Singleton
class Sparkle(private val canvas: Canvas, private val treeModel: TreeModel): Animation {

    private val sparkles = Array(treeModel.strips) { IntArray(treeModel.ledsPerStrip) }

    private val direction = Array(treeModel.strips) { BooleanArray(treeModel.ledsPerStrip) }

    private val sparkleColor = Array(treeModel.strips) { IntArray(treeModel.ledsPerStrip) }

    private var cnt: Double = 0.toDouble()

    private var white = false

    private val steps = arrayOf(255, 210, 180, 140, 110, 80, 40)

    override fun getFrame(seed: Long, frame: Int, nsPerFrame: Int): ByteArray {
        val color = if (white) {
            ColorUtils.makeColor(255, 255, 255)
        }
        else {
            color
        }
        for (x in sparkles.indices) {
            for (y in 0 until sparkles[x].size - 2) {
                val step = steps.indexOf(sparkles[x][y])
                if (step == -1) {
                    continue
                }
                if (step == steps.size - 1 && direction[x][y]) {
                    direction[x][y] = false
                }
                val brightness = if (step == 0) {
                    0
                }
                else if (direction[x][y]) {
                    steps[step + 1]
                }
                else {
                    steps[step - 1]
                }
                sparkles[x][y] = brightness
                sparkleColor[x][y] = ColorUtils.fadeColor(color, brightness)
                canvas.setValue(x, y + 1, sparkleColor[x][y])
            }
        }
        (0 until treeModel.strips).forEach {idx ->
            val y = CommonUtils.getRandom(treeModel.ledsPerStrip)
            sparkles[idx][y] = steps[steps.size - 1]
            sparkleColor[idx][y] = ColorUtils.fadeColor(color, steps[steps.size - 1])
            direction[idx][y] = true
        }
        return canvas.getValues()
    }

    val color: Int
        get() {
            cnt += Math.random() / 1000.0
            if (cnt > 1) {
                cnt--
            }
            return ColorUtils.makeColorHSB(cnt.toFloat(), 1f, 1f)
        }
}
