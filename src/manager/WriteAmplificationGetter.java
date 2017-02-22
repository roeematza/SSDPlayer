/*******************************************************************************
 * SSDPlayer Visualization Platform (Version 1.0)
 * Authors: Roman Shor, Gala Yadgar, Eitan Yaakobi, Assaf Schuster
 * Copyright (c) 2015, Technion � Israel Institute of Technology
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 * the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 *******************************************************************************/
package manager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ui.GeneralStatisticsGraph;
import ui.RegularHistoryGraph;
import entities.Device;
import entities.StatisticsColumn;
import entities.StatisticsGetter;

public class WriteAmplificationGetter implements StatisticsGetter {
	@Override
	public int getNumberOfColumns() {
		return 1;
	}

	@Override
	public List<StatisticsColumn> getStatistics(Device<?, ?, ?, ?> device) {
		double valueWA = computeWA(device);
		List<StatisticsColumn> list = new ArrayList<StatisticsColumn>();
		list.add(new StatisticsColumn("total writes to logical writes", 
										valueWA, false));
		return list;
	}

	@Override
	public GeneralStatisticsGraph getStatisticsGraph() {
		return new RegularHistoryGraph("Write Amplification", this, 1.5, 1);
	}
	
	public static double computeWA(Device<?, ?, ?, ?> device) {
		int total = device.getTotalMoved() + device.getTotalWritten();
		double valueWA = total==0 ? 1 : ((double)total)/device.getTotalWritten();
		return valueWA;
	}
	
	@Override
	public Entry<String, String> getInfoEntry(Device<?, ?, ?, ?> device) {
		return new AbstractMap.SimpleEntry("Write amplification", Double.toString(computeWA(device)));
	}
}