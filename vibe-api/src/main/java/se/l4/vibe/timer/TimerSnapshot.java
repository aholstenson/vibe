package se.l4.vibe.timer;

import se.l4.vibe.probes.ModifiableData;

/**
 * A sample of the state of a {@link Timer}.
 *  
 * @author Andreas Holstenson
 *
 */
public interface TimerSnapshot
	extends ModifiableData<TimerSnapshot>
{
	/**
	 * Get the total time measured as milliseconds.
	 * 
	 * @return
	 */
	long getTotalTimeInMs();
	
	/**
	 * Get the total time measured as nanoseconds.
	 * 
	 * @return
	 */
	long getTotalTimeInNs();
	
	/**
	 * Get how many times we have measured.
	 * 
	 * @return
	 */
	long getSamples();
	
	/**
	 * Get the average time in milliseconds.
	 * 
	 * @return
	 */
	double getAverageInMs();
	
	/**
	 * Get the average time in nanoseconds.
	 * 
	 * @return
	 */
	double getAverageInNs();
}
