BMSJava library
===================

* BMS Parser / Writer for java

* follows BSD license.

TODO
-------------------

* need to parse #BGAxx
* parse some more tag
* support comment
* need to change beatNumerator/Denominator method
* prevent changing LNOBJ to LN channel in SabunEditor

documentation
-------------------

##### BMSData

* BMSData.getBeatFromTime(int milisec) - returns beat(double) from time(milisec).
* BMSData.getTimeFromBeat(List<BMSKeyData> bpmarr, double beat) - returns beat(double) from beat. bpmarr can be gained using BMSUtil.ExtractChannel
* BMSData.getBPMFromBeat(double beat) - get BPM of beat.
* BMSData.getBPM(int val) - get BPM of EXT BPM Channel. if undefined, returns 0.
* BMSData.getSTOP(int val) - get STOP of EXT STOP Channel. if undefined, returns 0.
* BMSData.getBGA(int val) - get BMP channel filename. if undefined, returns "".
* BMSData.getWAV(int val) - get WAV channel filename. if undefined, returns "".
* BMSData.setBGA(int val, String s) - set BMP channel filename.
* BMSData.setWAV(int val, String s) - set WAV channel filename.
* BMSData.getBeatNumerator(int beat) - get Numerator of Beat. if undefined, returns 4.
* BMSData.getBeatDenominator(int beat) - get Denominator of Beat. if undefined, returns 4.
* BMSData.setNumeratorFit(BMSKeyData bkd, int fit) - change Numerator of beat and applies to beat data. <code>should be placed in BMSKeyData</code>
* BMSData.getBeatLength(int beat) - get relative length of beat.
* BMSData.getNotePosition(int beatHeight, int beat, int numerator) - get Note Position with beat and numerator relative to beatHeight, without considering BPM.
* BMSData.getNotePosition(int beatHeight, int beat, double decimal) - get Note Position with beat and decimal of beat relative to beatHeight, without considering BPM.
* BMSData.getNotePositionWithBPM(int beatHeight, List<BMSKeyData> bpmarr, double beat) - get Note Position with beat to beatHeight, considering BPM. base BPM is 130. bpmarr can be gained using BMSUtil.ExtractChannel.
* BMSData.fillNotePosition(List<BMSKeyData> arr, int beatHeight, boolean considerBPM) - fill all note's positionY data. beatHeight - relative beat height. considerBPM - determines note position should be considered or not.
* BMSData.getBeatFromPosition(int beatHeight, int sy) - with sy, returns beat relative to beatHeight without considering BPM.
* BMSData.getPairLN(BMSKeyData lnData) - returns another pair of LN BMSKeyData. if no pair exists, returns null.
* BMSData.isNoteAlreadyExists(int beat, int numerator, int channel, int layer) - check note is already exists. layer is only for BGM channel.
* BMSData.getNote(int beat, int numerator, int channel, int layer) - get note of current properties. if no note found, return null.
* BMSData.removeNote(int beat, int numerator, int channel, int layer) - remove note of current properties. if no removed, return false.
* BMSData.removeNote(BMSKeyData bkd) - remove note of argument. if no removed, return false.
* BMSData.removeChannel(int[] channel) - move notes from channels to BGM channel. useful when implementing SC auto.
* BMSData.is5Key() - check is 5 key BMS. returns boolean.
* BMSData.is10Key() - check is 10 key BMS. returns boolean.
* BMSData.checkKey() - check how many key this BMS need. returns integer.
* BMSData.getTotal() - get total. if no total, then calculate to default value.
* BMSData.addNote(BMSKeyData bkd) - add note to bmsdata.
* BMSData.dispose() - clear all note data for memory clean. (not BMS metadata, so you better use new method to clear fully.)
* BMSData.convertLNObj() - convert LNOBJs to Longnote. necessary method when play.

##### BMSUtil - all method are static

* BMSUtil.Log(String title, String desc) - Log for BMS. customize necessary.
* BMSUtil.CheckEncoding(byte[] BOM) - check encoding.
* BMSUtil.GetHash(byte[] data) - get hash value.
* BMSUtil.IsInteger(String str) - check is string is integer.
* BMSUtil.ExtHexToInt(String hex) - convert exthex(0-9, A-Z) to int.
* BMSUtil.IntToExtHex(int val) - convert int to exthex(0-9, A-Z).
* BMSUtil.HexToInt(String hex) - convert hex to int.
* BMSUtil.IntTo2Hex(int val) - convert int to hex of 2 character.
* BMSUtil.cloneKeyArray(List<BMSKeyData> bmsdata) - clone array
* BMSUtil.cloneKeyData(BMSKeyData bkd) - clone object
* BMSUtil.ExtractChannel(List<BMSKeyData> data, int channel) - extract channel
* BMSUtil.ExtractLayer(List<BMSKeyData> data, int layer) - extract layer. useful for BGM channel.

##### BMSKeyData 

* BMSKeyData.isLNFirst() - return boolean that represent this note is first of LN.
* BMSKeyData.getBeat() - returns beat of this note. data automatically filled when parsing BMS file.
* BMSKeyData.setBeat(double beat, BMSData bd) - set beat and numerator.
* BMSKeyData.getChannel() - get channel.
* BMSKeyData.setChannel(int val) - set channel.
* BMSKeyData.getValue() - get value.
* BMSKeyData.setValue(double val) - set value.
* BMSKeyData.setValue(int val) - set value.
* BMSKeyData.getAttr() - get attr. used when checking note is already pressed or note (playing)
* BMSKeyData.setAttr(int val) - set attr.
* BMSKeyData.isProcessed() - returns attr != 0
* BMSKeyData.setProcessed() - set attr = 1
* BMSKeyData.getPosY() - get positionY of note.
* BMSKeyData.getPosY(double precision) - get positionY of note, multiplying with argument.
* BMSKeyData.setPosY(double val) - set positionY of note.
* BMSKeyData.set1PKey(int val) - set 1P key. 1~7: key, 8:SC
* BMSKeyData.set2PKey(int val) - set 2P key. 1~7: key, 8:SC
* BMSKeyData.set1PTransKey(int val) - set 1P trans key. 1~7: key, 8:SC
* BMSKeyData.set2PTransKey(int val) - set 2P trans key. 1~7: key, 8:SC
* BMSKeyData.set1PLNKey(int val) - set 1P LN key. 1~7: key, 8:SC
* BMSKeyData.set2PLNKey(int val) - set 2P LN key. 1~7: key, 8:SC
* BMSKeyData.getKeyNum() - get key num.  1~7: key, 8:SC
* BMSKeyData.getKey() - get key. same as channel
* BMSKeyData.getChannel() - get key. same as channel
* BMSKeyData.getNumerator() - get numerator.
* BMSKeyData.setNumerator(int val) - set numerator.
* BMSKeyData.getLayerNum() - get layer num. useful for BGM channel
* BMSKeyData.setBGMChannel(int val) - set layernum.
* BMSKeyData.getTime() - get time of note. fill using BMSParser.setTimemark()
* BMSKeyData.isBPMChannel() - check if channel is 3.
* BMSKeyData.setBPMChannel() - set channel 3.
* BMSKeyData.isBPMExtChannel() - check if channel is 8.
* BMSKeyData.setBPMExtChannel() - set channel 8.
* BMSKeyData.isBGAChannel() - check if channel is 4.
* BMSKeyData.setBGAChannel() - set channel 4.
* BMSKeyData.isPoorChannel() - check if channel is 6.
* BMSKeyData.setPoorChannel() - set channel 6.
* BMSKeyData.isBGALayerChannel() - check if channel is 7.
* BMSKeyData.setBGALayerChannel() - set channel 7.
* BMSKeyData.isSTOPChannel() - check if channel is 9.
* BMSKeyData.setSTOPChannel() - set channel 9.
* BMSKeyData.is1PChannel() - check is 1P channel.
* BMSKeyData.is1PTransChannel() - check is 1P trans channel.
* BMSKeyData.is1PLNChannel() - check is 1P LN channel.
* BMSKeyData.is2PChannel() - check is 2P channel.
* BMSKeyData.is2PTransChannel() - check is 2P trans channel.
* BMSKeyData.is2PLNChannel() - check is 2P LN channel.

##### BMSParser - static

* BMSParser.LoadBMSFile(String path, BMSData bd) - load BMS file from path to BMSData bd.
* BMSParser.LoadBMSFile(byte[] data, BMSData bd) - load BMS file from data to BMSData bd.
* BMSParser.setTimemark(BMSData bd) - set time data of BMSKeyData.bmsdata. <code>should move to BMSData</code>
* BMSParser.ExecutePreProcessor(BMSData bd) - execute #IF~ #ENDIF commands of BMS. <code>need more work!</code>

warn: during parsing, BMSParser automatically changes BMSExtChannel to BMSChannel.

##### BMSWriter - static

* BMSWriter.SaveBMSFile(String path, BMSData bd) - write BMS file to path.


usage
-------------------

##### you can sort BMSKeyData using Collections

```java
Collections.sort(List<BMSKeyData> datas);
```

##### when you make bms emulator, first load file.

```java
if (!BMSParser.LoadBMSFile( path, Rhythmus.bmsData )) {
	return false;
}
```

and, do basic processes.

```java
BMSParser.ExecutePreProcessor(Rhythmus.bmsData);
bmsData.convertLNOBJ();
bmsData.checkKey();
BMSParser.setTimemark(Rhythmus.bmsData);
Rhythmus.bmsData.fillNotePosition(Rhythmus.bmsData.bmsdata, 100, true);
```

each time, you can get beat from eclipsed time, and get posY from eclipsed time to scroll notes.

```java
double nowBeat = Rhythmus.bmsData.getBeatFromTime(eclipsedTime);
List<BMSKeyData> bpmarray = BMSUtil.ExtractChannel(Rhythmus.bmsData.bmsdata, 3);
int pos_y = (int) (Rhythmus.bmsData.getNotePositionWithBPM(lainHeight, bpmarray, beat) * speed);
```

that's all. all you need to do is just draw notes!

```java
for (BMSKeyData bd: Rhythmus.bmsData.bmsdata) {
	if (bd.is1PChannel()) {
		int pos = d.getPosY(lainHeight / 100.0 * speed) - pos_y;
		drawNote(noteX[bd.getKeyNum()], pos);
	}
}
```

##### if you make bms editor with this, you can code like this -

```java
Rhythmus.bmsData.fillNotePosition(Rhythmus.bmsData.bmsdata, 100, false);
```

or play option with constant speed.


etc
-------------------

* this library is used in [SabunEditor](https://github.com/kuna/SabunEditor_Android) and [Rhythmus](https://github.com/kuna/Rhythmus_java)
* information from http://hitkey.nekokan.dyndns.info/cmds.htm
