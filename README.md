# PrinciplesOfCompilation
## 编译原理课设

### 基于java开发
#### （目前实现词法分析）
1.状态转换图

![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/StateTransitionDiagram.png)

2.字符对应token表

<table border='0' cellpadding='0' cellspacing='0' width='432' style='border-collapse: 
 collapse;table-layout:fixed;width:324pt'>
 <col width='72' span='6' style='width:54pt'>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r0'>
<td height='18' class='x21' width='72' style='height:13.5pt;width:54pt;'>类别</td>
<td class='x21' width='72' style='width:54pt;'>单词</td>
<td class='x21' width='72' style='width:54pt;'>编码</td>
<td class='x22' width='72' style='width:54pt;'>类别</td>
<td class='x22' width='72' style='width:54pt;'>单词</td>
<td class='x22' width='72' style='width:54pt;'>编码</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r1'>
<td rowspan='32' height='576' class='x24' style='height:432pt;'>关键字</td>
<td>char</td>
<td class='x21'>101</td>
<td rowspan='2' height='36' class='x24' style='height:27pt;'>单词类别</td>
<td>标识符</td>
<td class='x21'>700</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r2'>
<td>int</td>
<td class='x21'>102</td>
<td>实数</td>
<td class='x21'>800</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r3'>
<td>float</td>
<td class='x21'>103</td>
<td rowspan='37' height='666' class='x24' style='height:499.5pt;'>运算符</td>
<td>+</td>
<td class='x21'>201</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r4'>
<td>break</td>
<td class='x21'>104</td>
<td>-</td>
<td class='x21'>202</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r5'>
<td>const</td>
<td class='x21'>105</td>
<td>*</td>
<td class='x21'>203</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r6'>
<td>return</td>
<td class='x21'>106</td>
<td>/</td>
<td class='x21'>204</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r7'>
<td>void</td>
<td class='x21'>107</td>
<td>%</td>
<td class='x21'>205</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r8'>
<td>continue </td>
<td class='x21'>108</td>
<td class='x23'>++</td>
<td class='x21'>206</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r9'>
<td>do</td>
<td class='x21'>109</td>
<td class='x23'>--</td>
<td class='x21'>207</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r10'>
<td>while </td>
<td class='x21'>110</td>
<td>&gt;</td>
<td class='x21'>208</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r11'>
<td>if</td>
<td class='x21'>111</td>
<td>&gt;=</td>
<td class='x21'>209</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r12'>
<td>else</td>
<td class='x21'>112</td>
<td>&lt;</td>
<td class='x21'>210</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r13'>
<td>for</td>
<td class='x21'>113</td>
<td>&lt;=</td>
<td class='x21'>211</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r14'>
<td>auto</td>
<td class='x21'>114</td>
<td class='x23' x:str="'==">==</td>
<td class='x21'>212</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r15'>
<td>case</td>
<td class='x21'>115</td>
<td>!=</td>
<td class='x21'>213</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r16'>
<td>default</td>
<td class='x21'>116</td>
<td>&amp;&amp;</td>
<td class='x21'>214</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r17'>
<td>double</td>
<td class='x21'>117</td>
<td>||</td>
<td class='x21'>215</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r18'>
<td>enum</td>
<td class='x21'>118</td>
<td>!</td>
<td class='x21'>216</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r19'>
<td>extern</td>
<td class='x21'>119</td>
<td>&amp;</td>
<td class='x21'>217</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r20'>
<td>goto</td>
<td class='x21'>120</td>
<td>|</td>
<td class='x21'>218</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r21'>
<td>long</td>
<td class='x21'>121</td>
<td>~</td>
<td class='x21'>219</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r22'>
<td>register </td>
<td class='x21'>122</td>
<td>^</td>
<td class='x21'>220</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r23'>
<td>short</td>
<td class='x21'>123</td>
<td>&lt;&lt;</td>
<td class='x21'>221</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r24'>
<td>signed</td>
<td class='x21'>124</td>
<td>&gt;&gt;</td>
<td class='x21'>222</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r25'>
<td>sizeof</td>
<td class='x21'>125</td>
<td x:str="'=">=</td>
<td class='x21'>223</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r26'>
<td>static</td>
<td class='x21'>126</td>
<td class='x23'>+=</td>
<td class='x21'>224</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r27'>
<td>struct</td>
<td class='x21'>127</td>
<td class='x23'>-=</td>
<td class='x21'>225</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r28'>
<td>switch</td>
<td class='x21'>128</td>
<td>*=</td>
<td class='x21'>226</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r29'>
<td>typedef</td>
<td class='x21'>129</td>
<td>/=</td>
<td class='x21'>227</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r30'>
<td>union</td>
<td class='x21'>130</td>
<td>%=</td>
<td class='x21'>228</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r31'>
<td>unsigned </td>
<td class='x21'>131</td>
<td>&amp;=</td>
<td class='x21'>229</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r32'>
<td>volatile </td>
<td class='x21'>132</td>
<td>|=</td>
<td class='x21'>230</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r33'>
<td rowspan='4' height='72' class='x24' style='height:54pt;'>界符</td>
<td>{</td>
<td class='x21'>301</td>
<td>^=</td>
<td class='x21'>231</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r34'>
<td>}</td>
<td class='x21'>302</td>
<td>&gt;&gt;=</td>
<td class='x21'>232</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r35'>
<td>;</td>
<td class='x21'>303</td>
<td>&lt;&lt;=</td>
<td class='x21'>233</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r36'>
<td>,</td>
<td class='x21'>304</td>
<td>(</td>
<td class='x21'>234</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r37'>
<td rowspan='3' height='54' class='x24' style='height:40.5pt;'>单词类别</td>
<td>整数</td>
<td class='x21'>400</td>
<td>)</td>
<td class='x21'>235</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r38'>
<td>字符</td>
<td class='x21'>500</td>
<td>[</td>
<td class='x21'>236</td>
 </tr>
 <tr height='18' style='mso-height-source:userset;height:13.5pt' id='r39'>
<td>字符串</td>
<td class='x21'>600</td>
<td>]</td>
<td class='x21'>237</td>
 </tr>
<![if supportMisalignedColumns]>
 <tr height='0' style='display:none'>
  <td width='72' style='width:54pt'></td>
  <td width='72' style='width:54pt'></td>
  <td width='72' style='width:54pt'></td>
  <td width='72' style='width:54pt'></td>
  <td width='72' style='width:54pt'></td>
  <td width='72' style='width:54pt'></td>
 </tr>
 <![endif]>
</table>
