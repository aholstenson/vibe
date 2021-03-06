package se.l4.vibe.probes;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

/**
 * Probes for the JVM runtime.
 * 
 * @author Andreas Holstenson
 *
 */
public class JvmProbes
{
	private JvmProbes()
	{
	}
	
	/**
	 * Get a probe for measuring the CPU usage of this JVM process as factor
	 * between 0 to 1.
	 * 
	 * @return
	 */
	public static SampledProbe<Double> cpuUsage()
	{
		final com.sun.management.OperatingSystemMXBean os =
			(com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		
		final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		final Runtime rt = Runtime.getRuntime();
		
		return new AbstractSampledProbe<Double>()
		{
			private long lastUptime = runtime.getUptime();
			private long lastCpu = os.getProcessCpuTime();
			
			@Override
			public Double peek()
			{
				long uptime = runtime.getUptime();
				long cpu = os.getProcessCpuTime();
				
				if(lastUptime == 0)
				{
					lastUptime = uptime;
					lastCpu = cpu;
					return Double.NaN;
				}
				
				long elapsedCpu = cpu - lastCpu;
				long elapsedTime = uptime - lastUptime;
				
				// Calculate and return, do not store new values
				return Math.min(99, elapsedCpu / (elapsedTime * 10000.0 * rt.availableProcessors())) / 100;
			}
			
			@Override
			protected Double sample0()
			{
				long uptime = runtime.getUptime();
				long cpu = os.getProcessCpuTime();
				
				if(lastUptime == 0)
				{
					lastUptime = uptime;
					lastCpu = cpu;
					return Double.NaN;
				}
				
				long elapsedCpu = cpu - lastCpu;
				long elapsedTime = uptime - lastUptime;
				
				double v = Math.min(99, elapsedCpu / (elapsedTime * 10000.0 * rt.availableProcessors())) / 100;
				
				lastUptime = uptime;
				lastCpu = cpu;
				
				return v;
			}
		};
	}
	
	/**
	 * Get a probe for memory usage within the JVM.
	 * 
	 * @return
	 */
	public static SampledProbe<MemoryUsage> heapMemoryUsage()
	{
		final MemoryMXBean b = ManagementFactory.getMemoryMXBean();
		
		return new AbstractSampledProbe<MemoryUsage>()
		{
			@Override
			public MemoryUsage sample0()
			{
				return b.getHeapMemoryUsage();
			}
			
			@Override
			public MemoryUsage peek()
			{
				return sample0();
			}
		};
	}
	
	/**
	 * Get a probe for memory usage within the JVM.
	 * 
	 * @return
	 */
	public static SampledProbe<Long> totalUsedMemory()
	{
		final MemoryMXBean b = ManagementFactory.getMemoryMXBean();
		
		return new AbstractSampledProbe<Long>()
		{
			@Override
			public Long sample0()
			{
				return b.getHeapMemoryUsage().getUsed() + b.getNonHeapMemoryUsage().getUsed();
			}
			
			@Override
			public Long peek()
			{
				return sample0();
			}
		};
	}
	
	/**
	 * Get a probe for heap memory usage within the JVM.
	 * 
	 * @return
	 */
	public static SampledProbe<Double> heapMemoryAsFraction()
	{
		final MemoryMXBean b = ManagementFactory.getMemoryMXBean();
		
		return new AbstractSampledProbe<Double>()
		{
			@Override
			public Double sample0()
			{
				MemoryUsage heap = b.getHeapMemoryUsage();
				return heap.getUsed() / (double) heap.getMax();
			}
			
			@Override
			public Double peek()
			{
				return sample0();
			}
		};
	}

	/**
	 * Probe that measures the uptime of the JVM in milliseconds.
	 * 
	 * @return
	 */
	public static Probe<Long> uptime()
	{
		final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		
		return new Probe<Long>()
		{
			@Override
			public Long read()
			{
				return runtime.getUptime();
			}
		};
	}
	
	/**
	 * Get a probe suitable for sampling the number of threads active.
	 * 
	 * @return
	 */
	public static SampledProbe<Integer> threadCount()
	{
		final ThreadMXBean thread = ManagementFactory.getThreadMXBean();
		
		return new AbstractSampledProbe<Integer>()
		{
			@Override
			public Integer peek()
			{
				return sample0();
			}
			
			@Override
			protected Integer sample0()
			{
				return thread.getThreadCount();
			}
		};
	}
	
	/**
	 * Get the number of classes that are currently loaded by the JVM.
	 * 
	 * @return
	 */
	public static SampledProbe<Integer> loadedClassCount()
	{
		final ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
		
		return new AbstractSampledProbe<Integer>()
		{
			@Override
			public Integer peek()
			{
				return sample0();
			}
			
			@Override
			protected Integer sample0()
			{
				return cl.getLoadedClassCount();
			}
		};
	}
}
