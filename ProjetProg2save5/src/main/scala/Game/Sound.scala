package Sound
import java.net.URL
import javax.sound.sampled._
import Graphics2._

class Sound(name_file:String) {
	val file = name_file
	val url = new URL(app.get_path(file))
	val audioIn = AudioSystem.getAudioInputStream(url)
	val clip = AudioSystem.getClip
	clip.open(audioIn)
	
	var pause_position:Int= 0
	
	def play()={
		this.clip.setFramePosition(0)
		this.clip.start
	}
	
	def stop()={
		this.clip.stop
	}
	
	def pause()={
		this.pause_position = clip.getFramePosition
		this.clip.stop
	}
	
	def restart()={
		this.clip.setFramePosition(this.pause_position)
		this.clip.start
	}
	
	def loop_on()={
		//this.clip.setLoopPoints(0,this.clip.getFrameLength)
		this.clip.loop(10000) //Va boucler 10 000 fois
	}
	
	def loop_off()={
		this.clip.loop(0)
	}
}