package ttl;

/*
Copyright 2013 Kevin Robert Stravers

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


////////////////////////////////////////////////////////////
/// \brief Simple boolean with methods
///
////////////////////////////////////////////////////////////
public class Bool
{

    ////////////////////////////////////////////////////////////
    /// \brief Constructor
    ///
    /// Does not initialize the boolean
    ///
    ////////////////////////////////////////////////////////////
    public Bool()
    {
	
    }

    ////////////////////////////////////////////////////////////
    /// \brief Constructor
    ///
    /// Initializes the boolean
    ///
    ////////////////////////////////////////////////////////////
    public Bool ( final boolean arg )
    {
    	m_b = arg;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Sets the boolean to a value
    ///
    /// \param state The state that it will be set to
    ///
    ////////////////////////////////////////////////////////////
    public void reset(boolean state)
    {
    	m_b = state;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Set the boolean state
    ///
    /// \param state The state that it will be set to
    ///
    ////////////////////////////////////////////////////////////
    public void set ( boolean state )
    {
    	m_b = state;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Get the boolean state
    ///
    /// \return The state that the internal boolean has
    ///
    ////////////////////////////////////////////////////////////
    public boolean get()
    {
    	return m_b;
    }

    ////////////////////////////////////////////////////////////
    /// \brief Fetches the bool, then sets it to true
    ///
    /// \return the bool from before the set to true
    ///
    ////////////////////////////////////////////////////////////
    public boolean fetchAndEnable()
    {
        if (m_b)
        {
            return m_b;
        }
        else
        {
            m_b = true;
            return false;
        }
    }

    ////////////////////////////////////////////////////////////
    /// \brief Fetches the bool, then sets it to false
    ///
    /// \return the bool from before the set to false
    ///
    ////////////////////////////////////////////////////////////
    public boolean fetchAndDisable()
    {
        if (m_b)
        {
            m_b = false;
            return true;
        }
        else
        {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////
    /// \brief Fetches the bool, then flips the state
    ///
    /// \return the bool from before it was flipped
    ///
    ////////////////////////////////////////////////////////////
    public boolean fetchAndFlip()
    {
        if (m_b)
        {
            m_b = false;
            return true;
        }
        else
        {
            m_b = true;
            return false;
        }
    }

    ////////////////////////////////////////////////////////////
    /// \brief Fetches the bool, then sets it to a state
    ///
    /// \param state The state the bool wil be set to
    /// \return the bool from before the set to state
    ///
    ////////////////////////////////////////////////////////////
    public boolean fetchAndSet(boolean state)
    {
        if (m_b)
        {
            m_b = state;
            return true;
        }
        else
        {
            m_b = state;
            return false;
        }
    }

    private boolean m_b;

};