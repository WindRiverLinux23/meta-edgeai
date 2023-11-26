SUMMARY = "OpenVX Middleware library"
DESCRIPTION = "Builds tivision_apps user space library"

PR:append = "_edgeai_0"

LICENSE = "TI-TFL & BSD-2-Clause & BSD-3-Clause & BSD-4-Clause & MIT & Apache-2.0 & Apache-2.0-with-LLVM-exception & \
           Khronos & Hewlett-Packard & Patrick-Powell & FTL & Zlib & CC0-1.0 & OpenSSL"

LIC_FILES_CHKSUM = "file://${COREBASE}/../meta-ti/meta-ti-bsp/licenses/TI-TFL;md5=a1b59cb7ba626b9dbbcbf00f3fbc438a \
                    file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f \
                    file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COMMON_LICENSE_DIR}/BSD-4-Clause;md5=624d9e67e8ac41a78f6b6c2c55a83a2b \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COMMON_LICENSE_DIR}/Apache-2.0-with-LLVM-exception;md5=0bcd48c3bdfef0c9d9fd17726e4b7dab \
                    file://repo/tiovx/include/VX/vx.h;beginline=1;endline=15;md5=37315206223081f32a5b9aaaf912f637 \
                    file://${COREBASE}/../meta-ti/meta-ti-extras/licenses/Hewlett-Packard;md5=a07676ee09f5bfec457eb5ea75921d01 \
                    file://${COREBASE}/../meta-ti/meta-ti-extras/licenses/Patrick-Powell;md5=7e10716f13cff502f3cf6ebf8fe29c1e \
                    file://${COMMON_LICENSE_DIR}/FTL;md5=f0bf6b09ee8b02121ed10709d9e49d8b \
                    file://${COMMON_LICENSE_DIR}/Zlib;md5=87f239f408daca8a157858e192597633 \
                    file://${COMMON_LICENSE_DIR}/CC0-1.0;md5=0ceb3372c9595f0a8067e55da801e4a1 \
                    file://${COREBASE}/meta/files/common-licenses/OpenSSL;md5=4eb1764f3e65fafa1a25057f9082f2ae \
                    "

SRCREV_sdk_builder = "b2117af06c17861425d187853acbadffa8c3a4e8"
SRCREV_app_utils = "0dc760c1f91c090b10703884b95dbb92af87a694"
SRCREV_vision_apps = "8fb0db564053ea77da8f6f9bf9f3405137433cb0"
SRCREV_tiovx = "060d1d23300054acadab8aec5fad48f2aec06e1b"
SRCREV_imaging = "9da0de316257bcaf70fc2e5ec1844c61d7f0bcfa"
SRCREV_video_io = "ba6ce8f177f4ac73d9c5e8cbf6f3a50d2c6f7ce6"
SRCREV_ti-perception-toolkit = "7074e73756fecb1eaea59b8687bbf1642e51cb78"
SRCREV_psdk_include = "2dde83677ad4daf0d3e53bcd6d2a032a9bac53aa"
SRCREV_arm-tidl = "113dbdee90560ba86511b9ec2590430ceca861a7"
SRCREV_concerto = "87009d5386a0d642fcbcf5dccaf52d55898f8dc5"
TI_BRANCH = "main"

FILES:${PN} += "/opt/* \
                /usr/lib64/* \
"

SRC_URI = " \
git://git.ti.com/git/processor-sdk/sdk_builder.git;protocol=https;branch=${TI_BRANCH};branch=${TI_BRANCH};name=sdk_builder;destsuffix=repo/sdk_builder \
git://git.ti.com/git/processor-sdk/app_utils.git;protocol=https;branch=${TI_BRANCH};name=app_utils;destsuffix=repo/app_utils \
git://git.ti.com/git/processor-sdk/vision_apps.git;protocol=https;branch=${TI_BRANCH};name=vision_apps;destsuffix=repo/vision_apps \
git://git.ti.com/git/processor-sdk/tiovx.git;protocol=https;branch=${TI_BRANCH};name=tiovx;destsuffix=repo/tiovx \
git://git.ti.com/git/processor-sdk/imaging.git;protocol=https;branch=${TI_BRANCH};name=imaging;destsuffix=repo/imaging \
git://git.ti.com/git/processor-sdk/video_io.git;protocol=https;branch=${TI_BRANCH};name=video_io;destsuffix=repo/video_io \
git://git.ti.com/git/processor-sdk/ti-perception-toolkit.git;protocol=https;branch=${TI_BRANCH};name=ti-perception-toolkit;destsuffix=repo/ti-perception-toolkit \
git://git.ti.com/git/processor-sdk/psdk_include.git;protocol=https;branch=${TI_BRANCH};name=psdk_include;destsuffix=repo/psdk_include \
git://git.ti.com/git/processor-sdk-vision/arm-tidl.git;protocol=https;branch=master;name=arm-tidl;destsuffix=repo/psdk_include/tidl_j7/arm-tidl \
git://git.ti.com/git/processor-sdk/concerto.git;protocol=https;branch=${TI_BRANCH};name=concerto;destsuffix=repo/sdk_builder/concerto \
"
#PTK needs:
# EGL/egl.h
# glm/glm.hpp
# IL/il.h
# /usr/include/freetype2/ft2build.h
# ti_rpmsg_char.h
# dlr.h

DEPENDS = "glm devil freetype ti-rpmsg-char repo-native mesa-pvr libpam"
DEPENDS:remove:am62axx = " mesa-pvr"

COMPATIBLE_MACHINE = "j721e-evm|j721e-hs-evm|j721s2-evm|j721s2-hs-evm|j784s4-evm|j784s4-hs-evm|am62axx-evm|ti-j7"

PLAT_SOC = ""
PLAT_SOC:j721e = "j721e"
PLAT_SOC:ti-j72xx = "j721e"
PLAT_SOC:j721s2 = "j721s2"
PLAT_SOC:j784s4 = "j784s4"
PLAT_SOC:ti-j78xx = "j784s4"
PLAT_SOC:am62axx = "am62a"

S = "${WORKDIR}"

EXTRA_OEMAKE += "-C ${S}/repo/sdk_builder"

do_fetch[depends] += "repo-native:do_populate_sysroot"

do_compile() {
    CROSS_COMPILE_LINARO=aarch64-wrs-linux- \
    LINUX_SYSROOT_ARM=${STAGING_DIR_TARGET} \
    TREAT_WARNINGS_AS_ERROR=0 \
    GCC_LINUX_ARM_ROOT= \
    GCC_LINUX_ARM_ROOT_A72= \
    LINUX_FS_PATH=${STAGING_DIR_TARGET} \
    SOC=${PLAT_SOC} \
    oe_runmake yocto_build
}

do_install() {
    SOC=${PLAT_SOC} LINUX_FS_STAGE_PATH=${D} oe_runmake yocto_install
}

INSANE_SKIP:${PN} += "ldflags"

do_install:append:ti-j7 () {

    if [ ${libdir} = "/usr/lib64" ]; then
        mkdir -p ${D}/usr/lib64/
        mv ${D}/usr/lib/libtivision* ${D}/usr/lib64/
        rm -rf ${D}/usr/lib
    fi
}
