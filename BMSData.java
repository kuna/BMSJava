package com.kuna.sabuneditor_android.bms;

import java.util.ArrayList;
import java.util.List;

/*
 * TODO - REMOVE STOP and BPM (add to parser)
 * TODO - parse & store #BGA information.
 * TODO - store #IF~#ENDIF information successfully
 */

public class BMSData {
	public int player;
	public String title;
	public String subtitle;
	public String genre;
	public String artist;
	public int BPM;
	public int playlevel;
	public int difficulty;
	public int rank;
	public int total;
	public int volwav;
	public String stagefile;
	public String[] str_wav = new String[1322];
	public String[] str_bg = new String[1322];
	public double[] str_bpm = new double[1322];
	public double[] str_stop = new double[1322];
	public boolean[] LNObj = new boolean[1322];

	public int[] beat_numerator = new int[1024];		// MAXIMUM_BEAT
	public int[] beat_denominator = new int[1024];		// MAXIMUM_BEAT
	
	public List<BMSKeyData> bmsdata = new ArrayList<BMSKeyData>();	// MAXIMUM_OBJECT (Trans object+hit object+STOP+BPM)
	public List<BMSKeyData> bgadata = new ArrayList<BMSKeyData>();	// BGA
	public List<BMSKeyData> bgmdata = new ArrayList<BMSKeyData>();	// BGM
	public int notecnt;
	public double time;
	public int keycount;
	
	// bms file specific data
	public String hash;
	public String path;
	public String dir;
	public String preprocessCommand;
	
		
	// We dont store LNType
	// we always save LNTYPE 1 (with LNOBJ)

	public double getBeatFromTime(int millisec) {
		double bpm = BPM;
		double beat = 0;
		
		// for more precision set vals as Double
		double time = 0;
		double newtime = 0;
		
		for (int i=0; i<bmsdata.size(); i++) {
			BMSKeyData d = bmsdata.get(i);
			
			// Beat is effected by midi length ... check midi length
			while (d.beat > (int)beat+1) {
				newtime = time + ((int)beat+1-beat) * (1.0f/bpm*60*4) * 1000 * getBeatLength((int) beat);	// millisec
				if (newtime >= millisec) {
					return beat + (millisec-time)*(bpm/60000/4.0f)/getBeatLength((int) beat);
				}
				
				time = newtime;
				beat = (int)beat+1;
			}
			
			if (d.isSTOPChannel()) {	// STOP
				time += d.value * 1000;
				if (time >= millisec)
					return beat;
				continue;
			}
			
			if (d.isBPMChannel() || d.isBPMExtChannel()) {	// BPM
				newtime = time + (d.beat-beat) * (1.0f/bpm*60*4) * 1000 * getBeatLength((int) beat);	// millisec
				if (newtime >= millisec) {
					return beat + (millisec-time)*(bpm/60000/4.0f)/getBeatLength((int) beat);
				}
				
				beat = d.beat;
				bpm = d.value;
				time = newtime;
			}
		}
		
		// get beat from last beat
		beat += (millisec-time)*((double)bpm/60000/4.0f);
		
		// cannot be larger then last beat
		//double maxbeat = bmsdata.get(bmsdata.size()-1).beat;
		//if (beat > maxbeat)
		//	beat = maxbeat;
		
		return beat;
	}
	
	public double getTimeFromBeat(double beat) {
		double _bpm = BPM;		// BPM for parsing
		double _time = 0;		// time for parsing
		double _beat = 0;		// beat for parsing
		
		for (int i=0; i<bmsdata.size(); i++) {
			BMSKeyData d = bmsdata.get(i);
			
			// check midi length
			while (d.beat >= (int)_beat+1) {
				if ((int)_beat+1 > beat && beat > _beat) {
					return _time + ((int)_beat+1-beat) * (1.0f/_bpm*60) 
							* 4 * getBeatNumerator((int) beat) / getBeatDenominator((int) beat);
				}
				_time += ((int)_beat+1-_beat) * (1.0f/_bpm*60) 
						* 4 * getBeatNumerator((int) beat) / getBeatDenominator((int) beat);
				_beat = (int)_beat+1;
			}
			
			if (d.beat > beat && beat > _beat) {
				return _time + (beat - _beat) * (1.0f / _bpm * 60) 
						* 4 * getBeatNumerator((int) beat) / getBeatDenominator((int) beat);
			}
			_time += (d.beat - _beat) * (1.0f / _bpm * 60) 
					* 4 * getBeatNumerator((int) beat) / getBeatDenominator((int) beat);
			
			if (d.key == 3 || d.key == 8 )	// BPM
				_bpm = d.value;
			if (d.key == 9)
				_time += d.value;
			
			_beat = d.beat;
		}
		
		// time is over
		return _time;
	}
	
	public double getBPMFromBeat(double beat) {
		double bpm = BPM;
		for (int i=0; i<bmsdata.size(); i++) {
			if (bmsdata.get(i).beat > beat)
				break;
			if (bmsdata.get(i).key == 3 || bmsdata.get(i).key == 8)
				bpm = bmsdata.get(i).value;
		}
		return bpm;
	}
	
	public double getBPM(int val) {
		return str_bpm[val];
	}
	public double getSTOP(int val) {
		return str_stop[val];
	}
	public String getBGA(int val) {
		if (str_bg[val] == null)
			return "";
		return str_bg[val];
	}
	public String getWAV(int val) {
		if (str_wav[val] == null)
			return "";
		return str_wav[val];
	}
	public void setBGA(int val, String s) {
		str_bg[val] = s;
	}
	public void setWAV(int val, String s) {
		str_wav[val] = s;
	}

	public int getBeatNumerator(int beat) {
		if (beat_denominator[beat] == 0)
			return 4;
		else
			return beat_numerator[beat];
	}
	public int getBeatDenominator(int beat) {
		if (beat_denominator[beat] == 0)
			return 4;
		else
			return beat_denominator[beat];
	}

	public void setNumeratorFit(BMSKeyData bkd, int fit) {
		if (fit == 0)
			return;
		int divnum = 192 / fit;
		bkd.numerator = bkd.numerator - (bkd.numerator % divnum);
		bkd.beat = (int)(bkd.beat) + (double)bkd.numerator / (192 * getBeatNumerator((int) (bkd.beat)) / getBeatDenominator((int) (bkd.beat))); 
	}
	
	public double getBeatLength(int beat) {
		if (beat_denominator[beat] == 0)
			return 1;	// default
		return (double)beat_numerator[beat] / beat_denominator[beat];
	}
	
	public double getNotePosition(int beatHeight, int beat, int numerator) {
		int beatNum = 0;
		double r = 0;
		while (beatNum < beat) {
			// calculate new sbeatNum
			r += beatHeight * getBeatLength(beatNum);
			beatNum++;
		}
		
		r += beatHeight * getBeatLength(beatNum)
				* numerator / (192 * getBeatNumerator(beatNum) / getBeatDenominator(beatNum));
		return r;
	}
	
	public double getNotePosition(int beatHeight, int beat, double decimal) {
		int beatNum = 0;
		double r = 0;
		while (beatNum < beat) {
			// calculate new sbeatNum
			r += beatHeight * getBeatLength(beatNum);
			beatNum++;
		}
		
		r += beatHeight * getBeatLength(beatNum)
				* decimal;
		return r;
	}

	
	public double getNotePositionWithBPM(int beatHeight, List<BMSKeyData> bpmarr, double b) {
		// may need lots of performance
		int beat = (int)b;
		double decimal = b;
		double nbpm = BPM;
		
		int beatNum = 0;
		double beatDecimal = 0;
		int r = 0;
		for (BMSKeyData bpm: bpmarr) {
			while (beatNum < bpm.getBeat() && beatNum < beat) {
				r += beatHeight * getBeatLength(beatNum) * (1-beatDecimal) * nbpm;
				beatNum++;
				beatDecimal = 0;
			}
			if (beatNum == beat)
				break;
			
			beatDecimal = bpm.getValue() % 1;
			r += beatHeight * getBeatLength(beatNum) * beatDecimal * nbpm;
			nbpm = bpm.getValue();
		}
		
		// calculate left beat
		while (beatNum < beat) {
			r += beatHeight * getBeatLength(beatNum) * nbpm;
			beatNum++;
			beatDecimal = 0;
		}
		r += beatHeight * getBeatLength(beatNum) * nbpm * decimal;
		return r;
	}
	
	public void fillNotePosition(List<BMSKeyData> arr, int beatHeight, boolean considerBPM) {
		if (!considerBPM) {
			for (BMSKeyData bkd: arr) {
				bkd.setPosY( getNotePosition(beatHeight, (int)bkd.getBeat(), bkd.getBeat()%1) );
			}
		} else {
			List<BMSKeyData> bpmarr = BMSUtil.ExtractChannel(bmsdata, 3);	// BPM channel
			for (BMSKeyData bkd: arr) {
				bkd.setPosY( getNotePositionWithBPM(beatHeight, bpmarr, bkd.getBeat()) );
			}
		}
	}
	
	public BMSKeyData getBeatFromPosition(int beatHeight, int sy) {
		BMSKeyData bk = new BMSKeyData();
		
		int beatNum = 0;
		int y = 0, by = 0;
		while (y < sy) {
			// calculate new sbeatNum
			by = y;
			y += (int) (beatHeight * getBeatLength(beatNum));
			beatNum++;
		}
		
		y = by;
		beatNum--;
		
		double beat = beatNum;
		beat += (double)(sy-y) / beatHeight / getBeatLength(beatNum);
		
		bk.beat = beat;
		bk.numerator = (int)(
				(beat % 1) * 192 / getBeatDenominator(beatNum) * getBeatNumerator(beatNum)
				);
		
		return bk;
	}
	
	public BMSKeyData getPairLN(BMSKeyData lnData) {
		// get another LN pair
		BMSKeyData LNPair = null;
		boolean returnAtNext = false;
		
		for (BMSKeyData bkd: bmsdata) {
			if (bkd.getChannel() != lnData.getChannel())
				continue;
			
			if (returnAtNext)
				return bkd;
			
			if (bkd == lnData) {
				if (LNPair != null)
					return LNPair;
				else
					returnAtNext = true;
			}
			
			if (LNPair != null)
				LNPair = null;
			else
				LNPair = bkd;
		}
		
		return null;	// No matching LN pair found.
	}
	
	public boolean isNoteAlreadyExists(int beat, int numerator, int channel, int layer) {
		return (getNote(beat, numerator, channel, layer) != null);
	}
	
	public BMSKeyData getNote(int beat, int numerator, int channel, int layer) {
		if (channel == 1 /*BGM*/) {
			for (BMSKeyData bkd: bgmdata) {
				if ((int)bkd.beat == beat && bkd.numerator == numerator && bkd.getLayerNum() == layer)
					return bkd;
			}
			
			return null;
		} else if (channel == 4 || channel == 6 || channel == 7) {
			for (BMSKeyData bkd: bgadata) {
				if ((int)bkd.beat == beat && bkd.numerator == numerator && bkd.getChannel() == channel)
					return bkd;
			}
			
			return null;
		} else {
			for (BMSKeyData bkd: bmsdata) {
				if ((int)bkd.beat == beat && bkd.numerator == numerator && bkd.getChannel() == channel)
					return bkd;
			}
			
			return null;
		}
	}
	
	public boolean removeNote(int beat, int numerator, int channel, int layer) {
		if (channel == 1 /*BGM*/) {
			for (int i=0; i<bgmdata.size(); i++) {
				BMSKeyData bkd = bgmdata.get(i);
				if ((int)bkd.beat == beat && bkd.numerator == numerator && bkd.getLayerNum() == layer) {
					bgmdata.remove(i);
					return true;
				}
			}
			return false;
		} else if (channel == 4 || channel == 6 || channel == 7) {
			for (int i=0; i<bgadata.size(); i++) {
				BMSKeyData bkd = bgadata.get(i);
				if ((int)bkd.beat == beat && bkd.numerator == numerator && bkd.getChannel() == channel) {
					bgadata.remove(i);
					return true;
				}
			}
			return false;
		} else {
			for (int i=0; i<bmsdata.size(); i++) {
				BMSKeyData bkd = bmsdata.get(i);
				if ((int)bkd.beat == beat && bkd.numerator == numerator && bkd.getChannel() == channel) {
					bmsdata.remove(i);
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean removeNote(BMSKeyData bkd) {
		for (int i=0; i<bmsdata.size(); i++) {
			if (bmsdata.get(i) == bkd) {
				bmsdata.remove(i);
				return true;
			}
		}
		for (int i=0; i<bgadata.size(); i++) {
			if (bgadata.get(i) == bkd) {
				bgadata.remove(i);
				return true;
			}
		}
		for (int i=0; i<bgmdata.size(); i++) {
			if (bgmdata.get(i) == bkd) {
				bgmdata.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public void addNote(BMSKeyData bkd) {
		bmsdata.add(bkd);
	}
}
