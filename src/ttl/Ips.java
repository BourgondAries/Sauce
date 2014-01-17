/*
Copyright 2014 Kevin Robert Stravers

This file is part of TTL.

TTL is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

TTL is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with TTL.  If not, see <http://www.gnu.org/licenses/>.
*/


package ttl;
import java.util.Calendar;
/*
 * Changes from the C++ implementation:
 * 
 * Changed time from nanoseconds to milliseconds.
 * Added a check in the returned time (Java sometimes fails).
 */


////////////////////////////////////////////////////////////
/// \brief Iteration limiter. "Iterations Per Second"
///
/// Class encapsulating an iterative loop to limit its iterations/frames per second.
////////////////////////////////////////////////////////////
public class Ips
{

    ////////////////////////////////////////////////////////////
    /// \brief Constructor
    ///
    /// Initializes Ips with an unlimited framerate
    ///
    ////////////////////////////////////////////////////////////
    public Ips()
    {
    	min_time = 0;
    	delay = 0;
    	t1 = milliTime();
    	t2 = t1;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Constructor
    ///
    /// \param ips The amount of iterations per second that this object will allow
    ///
    ////////////////////////////////////////////////////////////
    public Ips(float ips)
    {
    	min_time = (long) (1E3f / ips);
    	delay = 0;
    	t1 = milliTime();
    	t2 = t1;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Destructor
    ///
    ////////////////////////////////////////////////////////////
    protected void finalize()
    {/*GC food, nothing to declare*/}

    ////////////////////////////////////////////////////////////
    /// \brief Iteration limiter
    ///
    /// A call to this function checks the time between this limit
    /// and the last limit. If the time is under a specified value,
    /// then this function will call sleep. The length of the sleep
    /// will be just so long so that the desired iterations per
    /// second will be achieved.
    ///
    ////////////////////////////////////////////////////////////
    public void limit() throws InterruptedException
    {
    	t2 = milliTime();
    	
    	delay = t2 - t1;
    	if (delay < 0) // Java may fail to get the correct clock
    		delay = 0; // so we skip the frame
    	
    	// We now know the delay, let's check it against our sleep time:
        if (delay < min_time)
            Thread.sleep((long) (min_time - delay));

        // Reset the starting timer.
        t1 = milliTime();
    }

    ////////////////////////////////////////////////////////////
    /// \brief Get the internal iterations per second value
    ///
    /// Method that returns the set iterations per second value.
    ///
    /// \return the number of iterations per second
    ///
    ////////////////////////////////////////////////////////////
    public float getIps()
    {
    	return 1E3f / min_time;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Set the internal iterations per second value
    ///
    /// A limit is deduced from the passed value since
    /// the limit and ips are intertwined. A limit of 33 ms approximates
    /// an ips value of 30. Setting either the limit or the ips
    /// are two ways of specifying a limit.
    ///
    /// \param ips The amount of iterations per second that this object will allow
    ///
    ////////////////////////////////////////////////////////////
    public void setIps(float ips)
    {
    	min_time = (long) (1E3f / ips);
    }

    ////////////////////////////////////////////////////////////
    /// \brief Returns the limit
    ///
    /// The limit is the minimum amount of time needed between two
    /// limit() calls in order to not invoke a sleep.
    ///
    /// \return the limit
    ///
    ////////////////////////////////////////////////////////////
    public long getMinIterationTime()
    {
    	return min_time;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Sets a new limit, undoes setIps()
    ///
    /// The limit is the minimum amount of time needed between two
    /// limit() calls in order to not invoke a sleep.
    /// The limit and ips are intertwined. A limit of 33 ms approximates
    /// an ips value of 30. Setting either the limit or the ips
    /// are two ways of specifying a limit.
    ///
    /// \param limit the limit
    ///
    ////////////////////////////////////////////////////////////
    public void setMinIterationTime(long limit)
    {
    	min_time = limit;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Returns the time between the last two limit calls
    ///
    /// The delay is the value of time used between two limit calls.
    /// This means that the value represents the running time of
    /// the loop surrounding the Ips object. This can be used to
    /// check how much time is used to run the loop.
    ///
    /// \return the delay in microseconds
    ///
    ////////////////////////////////////////////////////////////
    public long getDelay()
    {
    	return delay;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Serialization overload
    ///
    ////////////////////////////////////////////////////////////
    public String toString()
    {
    	StringBuilder str = new StringBuilder();
    	str.append("Ips:\n\tIT = ");

	    if (delay > h(1))
	    	str.append( delay / (1E6f * 3600.f) + " h" );
	    else if (delay > m(1))
	    	str.append( delay / (1E6f * 60.f) + " m" );
	    else if (delay > s(1))
	    	str.append( delay / (1E6f) + " s" );
	    else if (delay > ms(1))
	    	str.append( delay / (1E3f) + " ms" );
	    else
	    	str.append( delay + " µs" );
	    str.append( "\tRTPI = " );
	
	    if (min_time > h(1))
	    	str.append( delay / (1E6f * 3600.f) + " h" );
	    else if (min_time > m(1))
	    	str.append( delay / (1E6f * 60.f) + " m" );
	    else if (min_time > s(1))
	    	str.append( delay / (1E6f) + " s" );
	    else if (min_time > ms(1))
	    	str.append( delay / (1E3f) + " ms" );
	    else
	    	str.append( delay + " µs" );
	
	    return str.toString();
    }
    
    private long h(long x)
    {
    	return 3600 * 1000;
    }
    
    private long m(long x)
    {
    	return 60 * 1000;
    }
    
    private long s(long x)
    {
    	return 1000;
    }
    
    private long ms(long x)
    {
    	return 1;
    }
    
    private long milliTime()
    {
    	return (long) (Calendar.getInstance().get(Calendar.MILLISECOND));
    }

private

    long
        min_time, ///< The minimum amount of time per iteration requested
        delay, ///< The delay of last iteration
        t1, ///< Clock time since iteration start
        t2; ///< Clock time at iteration end
};



////////////////////////////////////////////////////////////
/// \class Ips
/// \ingroup Utilities
///
/// The Ips (Iterations per second) class is a generic frame- or iteration
/// limiting class. The class is minimalistic and simple to use:
///
/// \code
/// // Limit to 30 frames per second
/// ttl.Ips ips = new ttl.Ips(30.f);
///
/// for (int i = 0; i < 200; ++i)
/// {
///     System.out.println(i);
///
///     // Limit the speed
///     t.limit();
/// }
///
/// // Output IT (Iteration Time) and RTPI (Requested Time Per Iteration)
/// System.out.println(ips);
/// \endcode
///
////////////////////////////////////////////////////////////
