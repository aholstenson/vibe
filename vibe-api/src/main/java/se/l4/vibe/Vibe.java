package se.l4.vibe;


import se.l4.vibe.builder.EventsBuilder;
import se.l4.vibe.builder.ProbeBuilder;
import se.l4.vibe.builder.TimeSeriesBuilder;
import se.l4.vibe.probes.Probe;
import se.l4.vibe.probes.SampledProbe;

/**
 * Main interface for statistics and events.
 * 
 * @author Andreas Holstenson
 *
 */
public interface Vibe
{
	/**
	 * Export a new probe.
	 * 
	 * @param probe
	 * @return
	 */
	<T> ProbeBuilder<T> probe(Probe<T> probe);

	/**
	 * Start creating a new time series.
	 * 
	 * @return
	 */
	<T> TimeSeriesBuilder<T> timeSeries(SampledProbe<T> probe);
	
	/**
	 * Create a new events instance.
	 * 
	 * @param base
	 * @return
	 */
	<T> EventsBuilder<T> events(Class<T> base);
}
