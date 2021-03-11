# PrinciplesOfCompilation
## 编译原理课设

### 基于java开发

1.状态转换图

![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/StateTransitionDiagram.png)

2.字符对应token表

<table border=0 cellpadding=0 cellspacing=0 width=261 style='border-collapse:
 collapse;table-layout:fixed;width:195pt'>
 <col class=xl63 width=87 style='width:65pt'>
 <col width=87 style='width:65pt'>
 <col class=xl63 width=87 style='width:65pt'>
 <tr height=21 style='height:16.0pt'>
  <td height=21 class=xl63 width=87 style='height:16.0pt;width:65pt'>类<span
  style='mso-spacerun:yes'>  </span>别</td>
  <td class=xl63 width=87 style='width:65pt'>单<span style='mso-spacerun:yes'> 
  </span>词</td>
  <td class=xl63 width=87 style='width:65pt'>编<span style='mso-spacerun:yes'> 
  </span>码</td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td rowspan=13 height=273 class=xl63 style='height:208.0pt'>关键字</td>
  <td><span lang=EN-US>char</span></td>
  <td class=xl63><span lang=EN-US>101</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>int</span></td>
  <td class=xl63><span lang=EN-US>102</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>float</span></td>
  <td class=xl63><span lang=EN-US>103</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>break</span></td>
  <td class=xl63><span lang=EN-US>104</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>const</span></td>
  <td class=xl63><span lang=EN-US>105</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>return</span></td>
  <td class=xl63><span lang=EN-US>106</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>void</span></td>
  <td class=xl63><span lang=EN-US>107</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>continue</span></td>
  <td class=xl63><span lang=EN-US>108</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>do</span></td>
  <td class=xl63><span lang=EN-US>109</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>while</span></td>
  <td class=xl63><span lang=EN-US>110</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>if</span></td>
  <td class=xl63><span lang=EN-US>111</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>else</span></td>
  <td class=xl63><span lang=EN-US>112</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>for</span></td>
  <td class=xl63><span lang=EN-US>113</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td rowspan=4 height=84 class=xl63 style='height:64.0pt'>界符</td>
  <td><span lang=EN-US>{</span></td>
  <td class=xl63><span lang=EN-US>301</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>}</span></td>
  <td class=xl63><span lang=EN-US>302</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>;</span></td>
  <td class=xl63><span lang=EN-US>303</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>{</span></td>
  <td class=xl63><span lang=EN-US>304</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td rowspan=5 height=105 class=xl63 style='height:80.0pt'>单词类别</td>
  <td>整数</td>
  <td class=xl63><span lang=EN-US>400</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'>字符</td>
  <td class=xl63><span lang=EN-US>500</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'>字符串</td>
  <td class=xl63><span lang=EN-US>600</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'>标识符</td>
  <td class=xl63><span lang=EN-US>700</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'>实数</td>
  <td class=xl63><span lang=EN-US>800</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td rowspan=20 height=420 class=xl63 style='height:320.0pt'>运算符</td>
  <td><span lang=EN-US>(</span></td>
  <td class=xl63><span lang=EN-US>201</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>)</span></td>
  <td class=xl63><span lang=EN-US>202</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>[</span></td>
  <td class=xl63><span lang=EN-US>203</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>]</span></td>
  <td class=xl63><span lang=EN-US>204</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>!</span></td>
  <td class=xl63><span lang=EN-US>205</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>*</span></td>
  <td class=xl63><span lang=EN-US>206</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>/</span></td>
  <td class=xl63><span lang=EN-US>207</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>%</span></td>
  <td class=xl63><span lang=EN-US>208</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>+</span></td>
  <td class=xl63><span lang=EN-US>209</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>-</span></td>
  <td class=xl63><span lang=EN-US>210</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>&lt;&nbsp;</span></td>
  <td class=xl63><span lang=EN-US>211</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>&lt;=</span></td>
  <td class=xl63><span lang=EN-US>212</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>&gt;&nbsp;</span></td>
  <td class=xl63><span lang=EN-US>213</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>&gt;=</span></td>
  <td class=xl63><span lang=EN-US>214</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>==</span></td>
  <td class=xl63><span lang=EN-US>215</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>!=</span></td>
  <td class=xl63><span lang=EN-US>216</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>&amp;&amp;</span></td>
  <td class=xl63><span lang=EN-US>217</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>||</span></td>
  <td class=xl63><span lang=EN-US>218</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>=</span></td>
  <td class=xl63><span lang=EN-US>219</span></td>
 </tr>
 <tr height=21 style='height:16.0pt'>
  <td height=21 style='height:16.0pt'><span lang=EN-US>.</span></td>
  <td class=xl63><span lang=EN-US>220</span></td>
 </tr>
 <![if supportMisalignedColumns]>
 <tr height=0 style='display:none'>
  <td width=87 style='width:65pt'></td>
  <td width=87 style='width:65pt'></td>
  <td width=87 style='width:65pt'></td>
 </tr>
 <![endif]>
</table>
