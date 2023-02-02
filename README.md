 单周期CPU项目，Chisel编写，C++做仿真。目标：起uCore/rCore/XV6（未定）。



克隆方法：因为涉及到submodule，所以要使用`--recursive`：

```shell
git clone https://github.com/BJTU-NSCSCC-2023/prototype_core.git --recursive
```

之后请设置环境变量`NSCSCC2023_BJTU_PROTOTYPE_HOME`为本README.md所在路径。效果类似：

```shell
cd prototype_core
export NSCSCC2023_BJTU_PROTOTYPE_HOME=$(pwd)
```

**注意：这个只是个示范，请正确设置环境变量，不然shell脚本无法正常使用！**

之后执行`tool/startup.sh`来设置项目。
