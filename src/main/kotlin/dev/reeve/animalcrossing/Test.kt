package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.generation.riverAmplitude
import dev.reeve.animalcrossing.generation.riverFrequency
import dev.reeve.animalcrossing.generation.riverGenerator
import java.awt.Color
import java.awt.color.ColorSpace.TYPE_RGB
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.min

fun main() {
	val size = 256
	
	var largest = 0.0
	var smallest = 1.0
	
	val image = BufferedImage(size, size, TYPE_RGB)
	for (x in 0 until size) {
		for (y in 0 until size) {
			var i = abs(riverGenerator.noise(
					x.toDouble(),
					y.toDouble(),
					riverFrequency,
					riverAmplitude,
					true))
			
			i = min(i , 1.0)
			
			if (i > largest) largest = i
			if (i < smallest) smallest = i
			
			image.setRGB(x, y, Color(i.toFloat(), i.toFloat(), i.toFloat()).rgb)
		}
		println(largest)
	}
	ImageIO.write(image, "png", File("test.png"))
}