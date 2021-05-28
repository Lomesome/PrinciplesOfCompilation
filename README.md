# PrinciplesOfCompilation
## 重庆理工大学编译原理课设

### 基于java开发(以及Python、NASM)
#### 开发环境 Mac OS 、IDEA
#### 目前实现词法分析(包含手动构造及JFLEX自动构造)、语法分析、语义分析、中间代码生成、四元式优化、汇编代码生成及执行
#### 算法包含：LL(1)预测分析、算符优先、正规式转NFA、NFA转DFA、DFA转MFA、DAG优化
##### 待实现LR分析……

##### 有时间在来补充文档

1.字符对应token表

<table border=0 cellpadding=0 cellspacing=0 width=456 style='border-collapse:
 collapse;table-layout:fixed;width:341pt'>
 <col width=87 style='mso-width-source:userset;mso-width-alt:2773;width:65pt'>
 <col width=71 span=2 style='width:53pt'>
 <col width=85 style='mso-width-source:userset;mso-width-alt:2730;width:64pt'>
 <col width=71 span=2 style='width:53pt'>
 <tr height=20 style='height:15.0pt'>
  <td height=20 class=xl63 width=87 style='height:15.0pt;width:65pt'>类别</td>
  <td class=xl63 width=71 style='width:53pt'>单词</td>
  <td class=xl63 width=71 style='width:53pt'>编码</td>
  <td class=xl63 width=85 style='width:64pt'>类别</td>
  <td class=xl63 width=71 style='width:53pt'>单词</td>
  <td class=xl63 width=71 style='width:53pt'>编码</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td rowspan=32 height=640 class=xl63 style='height:480.0pt'>关键字</td>
  <td>char</td>
  <td class=xl63>101</td>
  <td rowspan=41 class=xl63>运算符</td>
  <td>(</td>
  <td class=xl63>201</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>int</td>
  <td class=xl63>102</td>
  <td>)</td>
  <td class=xl63>202</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>float</td>
  <td class=xl63>103</td>
  <td>[</td>
  <td class=xl63>203</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>break</td>
  <td class=xl63>104</td>
  <td>]</td>
  <td class=xl63>204</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>const</td>
  <td class=xl63>105</td>
  <td>!</td>
  <td class=xl63>205</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>return</td>
  <td class=xl63>106</td>
  <td>*</td>
  <td class=xl63>206</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>void</td>
  <td class=xl63>107</td>
  <td>/</td>
  <td class=xl63>207</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>continue<span
  style='mso-spacerun:yes'> </span></td>
  <td class=xl63>108</td>
  <td>%</td>
  <td class=xl63>208</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>do</td>
  <td class=xl63>109</td>
  <td>+</td>
  <td class=xl63>209</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>while<span
  style='mso-spacerun:yes'> </span></td>
  <td class=xl63>110</td>
  <td>-</td>
  <td class=xl63>210</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>if</td>
  <td class=xl63>111</td>
  <td>++</td>
  <td class=xl63>211</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>else</td>
  <td class=xl63>112</td>
  <td>--</td>
  <td class=xl63>212</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>for</td>
  <td class=xl63>113</td>
  <td>&lt;</td>
  <td class=xl63>213</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>auto</td>
  <td class=xl63>114</td>
  <td>&lt;=</td>
  <td class=xl63>214</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>case</td>
  <td class=xl63>115</td>
  <td>&gt;</td>
  <td class=xl63>215</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>default</td>
  <td class=xl63>116</td>
  <td>&gt;=</td>
  <td class=xl63>216</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>double</td>
  <td class=xl63>117</td>
  <td>==</td>
  <td class=xl63>217</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>enum</td>
  <td class=xl63>118</td>
  <td>!=</td>
  <td class=xl63>218</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>extern</td>
  <td class=xl63>119</td>
  <td>&amp;&amp;</td>
  <td class=xl63>219</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>goto</td>
  <td class=xl63>120</td>
  <td>||</td>
  <td class=xl63>220</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>long</td>
  <td class=xl63>121</td>
  <td>=</td>
  <td class=xl63>221</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>register<span
  style='mso-spacerun:yes'> </span></td>
  <td class=xl63>122</td>
  <td>+=</td>
  <td class=xl63>222</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>short</td>
  <td class=xl63>123</td>
  <td>-=</td>
  <td class=xl63>223</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>signed</td>
  <td class=xl63>124</td>
  <td>*=</td>
  <td class=xl63>224</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>sizeof</td>
  <td class=xl63>125</td>
  <td>/=</td>
  <td class=xl63>225</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>static</td>
  <td class=xl63>126</td>
  <td>%=</td>
  <td class=xl63>226</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>struct</td>
  <td class=xl63>127</td>
  <td>&amp;=</td>
  <td class=xl63>227</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>switch</td>
  <td class=xl63>128</td>
  <td>|=</td>
  <td class=xl63>228</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>typedef</td>
  <td class=xl63>129</td>
  <td>^=</td>
  <td class=xl63>229</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>union</td>
  <td class=xl63>130</td>
  <td>&gt;&gt;=</td>
  <td class=xl63>230</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>unsigned<span
  style='mso-spacerun:yes'> </span></td>
  <td class=xl63>131</td>
  <td>&lt;&lt;=</td>
  <td class=xl63>231</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>volatile<span
  style='mso-spacerun:yes'> </span></td>
  <td class=xl63>132</td>
  <td>&amp;</td>
  <td class=xl63>232</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td rowspan=4 height=80 class=xl63 style='height:60.0pt'>界符</td>
  <td>{</td>
  <td class=xl63>301</td>
  <td>|</td>
  <td class=xl63>233</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>}</td>
  <td class=xl63>302</td>
  <td>~</td>
  <td class=xl63>234</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>;</td>
  <td class=xl63>303</td>
  <td>^</td>
  <td class=xl63>235</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>,</td>
  <td class=xl63>304</td>
  <td>&lt;&lt;</td>
  <td class=xl63>236</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td rowspan=5 height=100 class=xl63 style='height:75.0pt'>单词类别</td>
  <td>整数</td>
  <td class=xl63>400</td>
  <td>&gt;&gt;</td>
  <td class=xl63>237</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>字符</td>
  <td class=xl63>500</td>
  <td>.</td>
  <td class=xl63>238</td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>字符串</td>
  <td class=xl63>600</td>
  <td colspan=2 style='mso-ignore:colspan'></td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>标识符</td>
  <td class=xl63>700</td>
  <td colspan=2 style='mso-ignore:colspan'></td>
 </tr>
 <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt'>实数</td>
  <td class=xl63>800</td>
  <td colspan=2 style='mso-ignore:colspan'></td>
 </tr>
 <![if supportMisalignedColumns]>
 <tr height=0 style='display:none'>
  <td width=87 style='width:65pt'></td>
  <td width=71 style='width:53pt'></td>
  <td width=71 style='width:53pt'></td>
  <td width=85 style='width:64pt'></td>
  <td width=71 style='width:53pt'></td>
  <td width=71 style='width:53pt'></td>
 </tr>
 <![endif]>
</table>

2.状态转换图

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/StateTransitionDiagram.png)

3.语义分析变量声明状态转移图

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/变量声明状态转移图.png)

#界面介绍

##打开工程

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/打开工程界面.png)
  
##新建工程

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/新建工程.png)
  
##词法分析

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/词法分析.png)
  
##语法分析

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/语法分析.png)
  
##语义分析

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/语义分析.png)
  
##语义分析及执行结果

  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/语义分析及执行结果.png)
 
##算符优先
 
  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/算符优先.png)
  
##正规式转NFA
 
  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/regtonfa.png)
  
##NFA转DFA
 
  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/nfatodfa.png)
  
##DFA转MFA
 
  ![Image text](https://github.com/Lomesome/PrinciplesOfCompilation/blob/master/dfatomfa.png)
  