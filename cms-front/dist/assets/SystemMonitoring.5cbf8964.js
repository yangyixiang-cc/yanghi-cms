var I=(d,e,c)=>new Promise((m,f)=>{var y=_=>{try{p(c.next(_))}catch(r){f(r)}},l=_=>{try{p(c.throw(_))}catch(r){f(r)}},p=_=>_.done?m(_.value):Promise.resolve(_.value).then(y,l);p((c=c.apply(d,e)).next())});/* empty css              *//* empty css               *//* empty css              */import{T as h}from"./tool.4bae35e7.js";import{_ as x}from"./_plugin-vue_export-helper.cdc0426e.js";import{c as P,r as j,o as M,f as S,g as z,h as i,i as o,j as s,k as n,t as a,m as w,B,ax as C,a1 as N,a3 as T,ay as U,az as V,aA as D,x as J,y as A}from"./index.04d82e8d.js";const u=d=>(J("data-v-4baf1c13"),d=d(),A(),d),E={class:"main"},G=u(()=>i("div",{class:"main-header"}," \u7CFB\u7EDF\u76D1\u63A7 ",-1)),H={class:"main-container"},q={class:"main-container-content"},F={class:"main-container-content-aricle"},K={class:"reset"},L=u(()=>i("br",null,null,-1)),O=u(()=>i("br",null,null,-1)),Q=u(()=>i("br",null,null,-1)),R=u(()=>i("br",null,null,-1)),W={__name:"SystemMonitoring",setup(d){const e=P({cupInfo:{},sysInfo:{},memoryInfo:{},diskUsePercent:{},diskInfos:[],sysInfo:{},jvmInfo:{}}),c=j(!1),m=()=>I(this,null,function*(){c.value=!0;const{data:l}=yield h.serverMonitor();l.code==200?(e.cupInfo=l.data.cupInfo,e.sysInfo=l.data.sysInfo,e.memoryInfo=l.data.memoryInfo,e.diskUsePercent=l.data.diskUsePercent,e.diskInfos=l.data.diskInfos,e.sysInfo=l.data.sysInfo,e.jvmInfo=l.data.jvmInfo,c.value=!1):(w.error("\u83B7\u53D6\u7CFB\u7EDF\u76D1\u63A7\u4FE1\u606F\u5931\u8D25"),c.value=!1)}),f=j(!1),y=()=>I(this,null,function*(){f.value=!0,yield m(),f.value=!1});return M(()=>I(this,null,function*(){yield m()})),(l,p)=>{const _=B,r=C,v=N,g=T,t=U,b=V,k=D;return S(),z("div",E,[G,i("div",H,[i("div",q,[i("div",F,[o(k,{spinning:c.value,tip:"\u6570\u636E\u52A0\u8F7D\u4E2D"},{default:s(()=>[i("div",K,[o(_,{type:"primary",loading:f.value,onClick:y},{default:s(()=>[n("\u5237\u65B0")]),_:1},8,["loading"])]),o(g,{gutter:[16,16],justify:"center"},{default:s(()=>[o(v,{span:6,class:"col"},{default:s(()=>[o(r,{type:"circle",percent:e.cupInfo.usePercent,"stroke-color":{from:"#108ee9",to:"#87d068"}},null,8,["percent"]),n(" CPU\u4F7F\u7528\u7387 ")]),_:1}),o(v,{span:6,class:"col"},{default:s(()=>[o(r,{type:"circle",percent:e.memoryInfo.usePercent,"stroke-color":{from:"#108ee9",to:"#87d068"}},null,8,["percent"]),n(" \u5185\u5B58\u4F7F\u7528\u7387 ")]),_:1}),o(v,{span:6,class:"col"},{default:s(()=>[o(r,{type:"circle",percent:e.diskUsePercent,"stroke-color":{"0%":"#108ee9","100%":"#87d068"}},null,8,["percent"]),n(" \u78C1\u76D8\u4F7F\u7528\u7387 ")]),_:1})]),_:1}),L,O,o(b,{bordered:"",title:"JVM\u865A\u62DF\u673A\u4FE1\u606F",size:l.size,column:{xxl:4,xl:3,lg:3,md:3,sm:2,xs:1}},{default:s(()=>[o(t,{label:"jdk\u7248\u672C"},{default:s(()=>[n(a(e.jvmInfo.jdkVersion),1)]),_:1}),o(t,{label:"jdk\u5B89\u88C5\u76EE\u5F55"},{default:s(()=>[n(a(e.jvmInfo.jdkHome),1)]),_:1}),o(t,{label:"jdk\u540D\u79F0"},{default:s(()=>[n(a(e.jvmInfo.jdkName),1)]),_:1}),o(t,{label:"\u603B\u5185\u5B58"},{default:s(()=>[n(a(e.jvmInfo.jvmTotalMemory),1)]),_:1}),o(t,{label:"Java\u865A\u62DF\u673A\u5C06\u5C1D\u8BD5\u4F7F\u7528\u7684\u6700\u5927\u5185\u5B58\u91CF"},{default:s(()=>[n(a(e.jvmInfo.maxMemory),1)]),_:1}),o(t,{label:"\u7A7A\u95F2\u5185\u5B58"},{default:s(()=>[n(a(e.jvmInfo.freeMemory),1)]),_:1}),o(t,{label:"\u5DF2\u4F7F\u7528\u5185\u5B58"},{default:s(()=>[n(a(e.jvmInfo.usedMemory),1)]),_:1}),o(t,{label:"\u5185\u5B58\u4F7F\u7528\u7387"},{default:s(()=>[n(a(e.jvmInfo.usePercent),1)]),_:1}),o(t,{label:"\u5206\u533A\u5927\u5C0F\uFF08\u5B57\u7B26\u4E32\u7C7B\u578B\uFF0C\u4F8B\u5982 1GB\uFF09"},{default:s(()=>[n(a(e.jvmInfo.size),1)]),_:1}),o(t,{label:"\u8FD4\u56DEJava\u865A\u62DF\u673A\u7684\u6B63\u5E38\u8FD0\u884C\u65F6\u95F4\uFF08\u6BEB\u79D2\uFF09"},{default:s(()=>[n(a(e.jvmInfo.uptime),1)]),_:1}),o(t,{label:"\u8FD4\u56DEJava\u865A\u62DF\u673A\u7684\u542F\u52A8\u65F6\u95F4\uFF08\u6BEB\u79D2\uFF09\u3002"},{default:s(()=>[n(a(e.jvmInfo.startTime),1)]),_:1})]),_:1},8,["size"]),Q,R,o(b,{bordered:"",title:"\u670D\u52A1\u5668\u4FE1\u606F",size:l.size,column:{xxl:4,xl:3,lg:3,md:3,sm:2,xs:1}},{default:s(()=>[o(t,{label:"\u670D\u52A1\u5668\u540D\u79F0"},{default:s(()=>[n(a(e.sysInfo.name),1)]),_:1}),o(t,{label:"IP\u5730\u5740"},{default:s(()=>[n(a(e.sysInfo.ip),1)]),_:1}),o(t,{label:"\u64CD\u4F5C\u7CFB\u7EDF"},{default:s(()=>[n(a(e.sysInfo.osName),1)]),_:1}),o(t,{label:"\u7CFB\u7EDF\u67B6\u6784"},{default:s(()=>[n(a(e.sysInfo.osArch),1)]),_:1}),o(t,{label:"\u9879\u76EE\u8DEF\u5F84"},{default:s(()=>[n(a(e.sysInfo.userDir),1)]),_:1}),o(t,{label:"\u5185\u6838"},{default:s(()=>[n(a(e.cupInfo.physicalProcessorCount),1)]),_:1}),o(t,{label:"\u903B\u8F91\u5904\u7406\u5668"},{default:s(()=>[n(a(e.cupInfo.logicalProcessorCount),1)]),_:1})]),_:1},8,["size"])]),_:1},8,["spinning"])])])])])}}},ne=x(W,[["__scopeId","data-v-4baf1c13"]]);export{ne as default};
