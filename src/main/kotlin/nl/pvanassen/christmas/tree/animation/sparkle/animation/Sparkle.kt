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

    private val sparkleColor = Array(treeModel.strips) { IntArray(treeModel.ledsPerStrip) }

    private var cnt: Double = 0.toDouble()

    private var white = false

    override fun getFrame(seed: Long, frame: Int, nsPerFrame: Int): ByteArray {
        val color = if (white) {
            ColorUtils.makeColor(255, 255, 255)
        }
        else {
            color
        }
        for (x in sparkles.indices) {
            for (y in 0 until sparkles[x].size - 2) {
                when {
                    sparkles[x][y] == 255 -> {
                        sparkles[x][y] = 126
                        sparkleColor[x][y] = ColorUtils.fadeColor(color, 126)
                    }
                    sparkles[x][y] == 126 -> {
                        sparkles[x][y] = 0
                        sparkleColor[x][y] = 0
                    }
                    sparkles[x][y] == 128 -> {
                        sparkles[x][y] = 255
                        sparkleColor[x][y] = ColorUtils.fadeColor(color, 255)
                    }
                }
                canvas.setValue(x, y + 1, sparkleColor[x][y])
            }
        }
        for (i in 0..2) {
            (0 until treeModel.strips).forEach {idx ->
                val y = CommonUtils.getRandom(treeModel.ledsPerStrip)
                sparkles[idx][y] = 128
                sparkleColor[idx][y] = ColorUtils.fadeColor(color, 128)
            }
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
