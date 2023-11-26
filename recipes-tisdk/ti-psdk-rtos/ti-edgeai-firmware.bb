SUMMARY = "TI RTOS prebuilt binary firmware images for EdgeAI"

LICENSE = "TI-TFL"
LIC_FILES_CHKSUM = "file://${COREBASE}/../meta-ti/meta-ti-bsp/licenses/TI-TFL;md5=a1b59cb7ba626b9dbbcbf00f3fbc438a"

COMPATIBLE_MACHINE = "j721e-evm|j721e-hs-evm|j721s2-evm|j721s2-hs-evm|j784s4-evm|j784s4-hs-evm|am62axx-evm|ti-j7"

PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_DEFAULT_DEPS = "1"

inherit deploy
inherit update-alternatives

PLAT_SFX = ""
PLAT_SFX:j721e = "j721e"
PLAT_SFX:ti-j72xx = "j721e"
PLAT_SFX:ti-j78xx = "j784s4"
PLAT_SFX:j721s2 = "j721s2"
PLAT_SFX:j784s4 = "j784s4"
PLAT_SFX:am62axx = "am62a"

SRCREV = "bfbcadc689fbb0be7deb161d287ab550a356b339"
BRANCH = "main"

SRC_URI = " \
    git://git.ti.com/git/processor-sdk/psdk_fw.git;protocol=https;branch=${BRANCH} \
"

S = "${WORKDIR}/git"

PV = "${SRCPV}"

# Secure Build
inherit ti-secdev

DEPENDS:append:ti-j7 = "${@ '' if d.getVar('TI_SECURE_DEV_PKG_K3') else ' ti-k3-secdev-native' }"
TI_K3_SECDEV_INSTALL_DIR:ti-j7 = "${STAGING_DIR_NATIVE}${datadir}/ti/ti-k3-secdev"
TI_SECURE_DEV_PKG:ti-j7 = "${@ d.getVar('TI_SECURE_DEV_PKG_K3') or d.getVar('TI_K3_SECDEV_INSTALL_DIR') }"

FW_DIR:edgeai = "${PLAT_SFX}/vision_apps_eaik"
FW_DIR:adas = "${PLAT_SFX}/vision_apps_evm"
FW_DIR:ti-j7 = "${PLAT_SFX}/vision_apps_evm"

INSTALL_FW_DIR:edgeai = "${nonarch_base_libdir}/firmware/vision_apps_eaik/"
INSTALL_FW_DIR:adas = "${nonarch_base_libdir}/firmware/vision_apps_evm/"
INSTALL_FW_DIR:ti-j7 = "${nonarch_base_libdir}/firmware/vision_apps_evm/"

MCU_1_0_FW = "vx_app_rtos_linux_mcu1_0.out"
MCU_1_1_FW = "vx_app_rtos_linux_mcu1_1.out"
MCU_2_0_FW = "vx_app_rtos_linux_mcu2_0.out"
MCU_2_1_FW = "vx_app_rtos_linux_mcu2_1.out"
MCU_3_0_FW = "vx_app_rtos_linux_mcu3_0.out"
MCU_3_1_FW = "vx_app_rtos_linux_mcu3_1.out"
MCU_4_0_FW = "vx_app_rtos_linux_mcu4_0.out"
MCU_4_1_FW = "vx_app_rtos_linux_mcu4_1.out"
C66_1_FW   = "vx_app_rtos_linux_c6x_1.out"
C66_2_FW   = "vx_app_rtos_linux_c6x_2.out"
C7X_1_FW   = "vx_app_rtos_linux_c7x_1.out"
C7X_2_FW   = "vx_app_rtos_linux_c7x_2.out"
C7X_3_FW   = "vx_app_rtos_linux_c7x_3.out"
C7X_4_FW   = "vx_app_rtos_linux_c7x_4.out"

FW_LIST = ""
FW_LIST:j721e =     "              ${MCU_1_1_FW} ${MCU_2_0_FW} ${MCU_2_1_FW} ${MCU_3_0_FW} ${MCU_3_1_FW}                             ${C66_1_FW} ${C66_2_FW} ${C7X_1_FW}"
FW_LIST:ti-j72xx =     "              ${MCU_1_1_FW} ${MCU_2_0_FW} ${MCU_2_1_FW} ${MCU_3_0_FW} ${MCU_3_1_FW}                             ${C66_1_FW} ${C66_2_FW} ${C7X_1_FW}"
FW_LIST:j721s2 =    "              ${MCU_1_1_FW} ${MCU_2_0_FW} ${MCU_2_1_FW} ${MCU_3_0_FW} ${MCU_3_1_FW}                                                     ${C7X_1_FW} ${C7X_2_FW}"
FW_LIST:j784s4 =    "              ${MCU_1_1_FW} ${MCU_2_0_FW} ${MCU_2_1_FW} ${MCU_3_0_FW} ${MCU_3_1_FW} ${MCU_4_0_FW} ${MCU_4_1_FW}                         ${C7X_1_FW} ${C7X_2_FW} ${C7X_3_FW} ${C7X_4_FW}"
FW_LIST:ti-j78xx =    "              ${MCU_1_1_FW} ${MCU_2_0_FW} ${MCU_2_1_FW} ${MCU_3_0_FW} ${MCU_3_1_FW} ${MCU_4_0_FW} ${MCU_4_1_FW}                         ${C7X_1_FW} ${C7X_2_FW} ${C7X_3_FW} ${C7X_4_FW}"
FW_LIST:am62axx =   "${MCU_1_0_FW}                                                                                                                           ${C7X_1_FW}"

do_install() {
    # Sign the firmware
    for FW_NAME in ${FW_LIST}
    do
        ${TI_SECURE_DEV_PKG}/scripts/secure-binary-image.sh ${S}/${FW_DIR}/${FW_NAME} ${S}/${FW_DIR}/${FW_NAME}.signed
    done

    # Install the Firmware
    install -d ${D}${INSTALL_FW_DIR}
    for FW_NAME in ${FW_LIST}
    do
        install -m 0644 ${S}/${FW_DIR}/${FW_NAME}        ${D}${INSTALL_FW_DIR}
        install -m 0644 ${S}/${FW_DIR}/${FW_NAME}.signed ${D}${INSTALL_FW_DIR}
    done
}

# For am62a DM FW name in u-boot recipe is hard coded to ipc_echo_testb_mcu1_0_release_strip.xer5f
do_install:append:am62axx() {
    mkdir -p ${D}${nonarch_base_libdir}/firmware/ti-dm/am62axx/
    cp ${D}${INSTALL_FW_DIR}${MCU_1_0_FW} ${D}${nonarch_base_libdir}/firmware/ti-dm/am62axx/ipc_echo_testb_mcu1_0_release_strip.xer5f
    cp ${D}${INSTALL_FW_DIR}${MCU_1_0_FW}.signed ${D}${nonarch_base_libdir}/firmware/ti-dm/am62axx/ipc_echo_testb_mcu1_0_release_strip.xer5f.signed
}

do_deploy() {
}

do_deploy:am62axx() {
    # DM Firmware is needed for rebuilding U-Boot
    install -d ${DEPLOYDIR}/ti-dm/am62axx
    cp ${D}${INSTALL_FW_DIR}${MCU_1_0_FW} ${DEPLOYDIR}/ti-dm/am62axx/ipc_echo_testb_mcu1_0_release_strip.xer5f
    cp ${D}${INSTALL_FW_DIR}${MCU_1_0_FW}.signed ${DEPLOYDIR}/ti-dm/am62axx/ipc_echo_testb_mcu1_0_release_strip.xer5f.signed
}

# Set up names for the firmwares
ALTERNATIVE:${PN}:j721e = "\
                    j7-mcu-r5f0_1-fw \
                    j7-main-r5f0_0-fw \
                    j7-main-r5f0_1-fw \
                    j7-main-r5f1_0-fw \
                    j7-main-r5f1_1-fw \
                    j7-c66_0-fw \
                    j7-c66_1-fw \
                    j7-c71_0-fw\
                    j7-main-r5f0_0-fw-sec \
                    j7-main-r5f0_1-fw-sec \
                    j7-main-r5f1_0-fw-sec \
                    j7-main-r5f1_1-fw-sec \
                    j7-c66_0-fw-sec \
                    j7-c66_1-fw-sec \
                    j7-c71_0-fw-sec \
                    "
ALTERNATIVE:${PN}:ti-j72xx = "\
                    j7-mcu-r5f0_1-fw \
                    j7-main-r5f0_0-fw \
                    j7-main-r5f0_1-fw \
                    j7-main-r5f1_0-fw \
                    j7-main-r5f1_1-fw \
                    j7-c66_0-fw \
                    j7-c66_1-fw \
                    j7-c71_0-fw\
                    j7-main-r5f0_0-fw-sec \
                    j7-main-r5f0_1-fw-sec \
                    j7-main-r5f1_0-fw-sec \
                    j7-main-r5f1_1-fw-sec \
                    j7-c66_0-fw-sec \
                    j7-c66_1-fw-sec \
                    j7-c71_0-fw-sec \
                    "   

ALTERNATIVE:${PN}:j721s2 = "\
                    j721s2-mcu-r5f0_1-fw \
                    j721s2-main-r5f0_0-fw \
                    j721s2-main-r5f0_1-fw \
                    j721s2-main-r5f1_0-fw \
                    j721s2-main-r5f1_1-fw \
                    j721s2-c71_0-fw \
                    j721s2-c71_1-fw \
                    j721s2-main-r5f0_0-fw-sec \
                    j721s2-main-r5f0_1-fw-sec \
                    j721s2-main-r5f1_0-fw-sec \
                    j721s2-main-r5f1_1-fw-sec \
                    j721s2-c71_0-fw-sec \
                    j721s2-c71_1-fw-sec \
                    "

ALTERNATIVE:${PN}:j784s4 = "\
                    j784s4-mcu-r5f0_1-fw \
                    j784s4-main-r5f0_0-fw \
                    j784s4-main-r5f0_1-fw \
                    j784s4-main-r5f1_0-fw \
                    j784s4-main-r5f1_1-fw \
                    j784s4-main-r5f2_0-fw \
                    j784s4-main-r5f2_1-fw \
                    j784s4-c71_0-fw \
                    j784s4-c71_1-fw \
                    j784s4-c71_2-fw \
                    j784s4-c71_3-fw \
                    "
ALTERNATIVE:${PN}:ti-j78xx = "\
                    j784s4-mcu-r5f0_1-fw \
                    j784s4-main-r5f0_0-fw \
                    j784s4-main-r5f0_1-fw \
                    j784s4-main-r5f1_0-fw \
                    j784s4-main-r5f1_1-fw \
                    j784s4-main-r5f2_0-fw \
                    j784s4-main-r5f2_1-fw \
                    j784s4-c71_0-fw \
                    j784s4-c71_1-fw \
                    j784s4-c71_2-fw \
                    j784s4-c71_3-fw \
                    " 
ALTERNATIVE:${PN}:am62axx = "\
                    am62a-c71_0-fw \
                    "

# Set up link names for the firmwares

ALTERNATIVE_LINK_NAME[j7-mcu-r5f0_1-fw] = "${nonarch_base_libdir}/firmware/j7-mcu-r5f0_1-fw"
ALTERNATIVE_LINK_NAME[j7-main-r5f0_0-fw] = "${nonarch_base_libdir}/firmware/j7-main-r5f0_0-fw"
ALTERNATIVE_LINK_NAME[j7-main-r5f0_1-fw] = "${nonarch_base_libdir}/firmware/j7-main-r5f0_1-fw"
ALTERNATIVE_LINK_NAME[j7-main-r5f1_0-fw] = "${nonarch_base_libdir}/firmware/j7-main-r5f1_0-fw"
ALTERNATIVE_LINK_NAME[j7-main-r5f1_1-fw] = "${nonarch_base_libdir}/firmware/j7-main-r5f1_1-fw"
ALTERNATIVE_LINK_NAME[j7-c66_0-fw] = "${nonarch_base_libdir}/firmware/j7-c66_0-fw"
ALTERNATIVE_LINK_NAME[j7-c66_1-fw] = "${nonarch_base_libdir}/firmware/j7-c66_1-fw"
ALTERNATIVE_LINK_NAME[j7-c71_0-fw] = "${nonarch_base_libdir}/firmware/j7-c71_0-fw"

ALTERNATIVE_LINK_NAME[j7-main-r5f0_0-fw-sec] = "${nonarch_base_libdir}/firmware/j7-main-r5f0_0-fw-sec"
ALTERNATIVE_LINK_NAME[j7-main-r5f0_1-fw-sec] = "${nonarch_base_libdir}/firmware/j7-main-r5f0_1-fw-sec"
ALTERNATIVE_LINK_NAME[j7-main-r5f1_0-fw-sec] = "${nonarch_base_libdir}/firmware/j7-main-r5f1_0-fw-sec"
ALTERNATIVE_LINK_NAME[j7-main-r5f1_1-fw-sec] = "${nonarch_base_libdir}/firmware/j7-main-r5f1_1-fw-sec"
ALTERNATIVE_LINK_NAME[j7-c66_0-fw-sec] = "${nonarch_base_libdir}/firmware/j7-c66_0-fw-sec"
ALTERNATIVE_LINK_NAME[j7-c66_1-fw-sec] = "${nonarch_base_libdir}/firmware/j7-c66_1-fw-sec"
ALTERNATIVE_LINK_NAME[j7-c71_0-fw-sec] = "${nonarch_base_libdir}/firmware/j7-c71_0-fw-sec"

ALTERNATIVE_LINK_NAME[j721s2-mcu-r5f0_1-fw] = "${nonarch_base_libdir}/firmware/j721s2-mcu-r5f0_1-fw"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f0_0-fw] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f0_0-fw"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f0_1-fw] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f0_1-fw"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f1_0-fw] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f1_0-fw"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f1_1-fw] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f1_1-fw"
ALTERNATIVE_LINK_NAME[j721s2-c71_0-fw] = "${nonarch_base_libdir}/firmware/j721s2-c71_0-fw"
ALTERNATIVE_LINK_NAME[j721s2-c71_1-fw] = "${nonarch_base_libdir}/firmware/j721s2-c71_1-fw"

ALTERNATIVE_LINK_NAME[j721s2-main-r5f0_0-fw-sec] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f0_0-fw-sec"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f0_1-fw-sec] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f0_1-fw-sec"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f1_0-fw-sec] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f1_0-fw-sec"
ALTERNATIVE_LINK_NAME[j721s2-main-r5f1_1-fw-sec] = "${nonarch_base_libdir}/firmware/j721s2-main-r5f1_1-fw-sec"
ALTERNATIVE_LINK_NAME[j721s2-c71_0-fw-sec] = "${nonarch_base_libdir}/firmware/j721s2-c71_0-fw-sec"
ALTERNATIVE_LINK_NAME[j721s2-c71_1-fw-sec] = "${nonarch_base_libdir}/firmware/j721s2-c71_1-fw-sec"

ALTERNATIVE_LINK_NAME[j784s4-mcu-r5f0_1-fw] = "${nonarch_base_libdir}/firmware/j784s4-mcu-r5f0_1-fw"
ALTERNATIVE_LINK_NAME[j784s4-main-r5f0_0-fw] = "${nonarch_base_libdir}/firmware/j784s4-main-r5f0_0-fw"
ALTERNATIVE_LINK_NAME[j784s4-main-r5f0_1-fw] = "${nonarch_base_libdir}/firmware/j784s4-main-r5f0_1-fw"
ALTERNATIVE_LINK_NAME[j784s4-main-r5f1_0-fw] = "${nonarch_base_libdir}/firmware/j784s4-main-r5f1_0-fw"
ALTERNATIVE_LINK_NAME[j784s4-main-r5f1_1-fw] = "${nonarch_base_libdir}/firmware/j784s4-main-r5f1_1-fw"
ALTERNATIVE_LINK_NAME[j784s4-main-r5f2_0-fw] = "${nonarch_base_libdir}/firmware/j784s4-main-r5f2_0-fw"
ALTERNATIVE_LINK_NAME[j784s4-main-r5f2_1-fw] = "${nonarch_base_libdir}/firmware/j784s4-main-r5f2_1-fw"
ALTERNATIVE_LINK_NAME[j784s4-c71_0-fw] = "${nonarch_base_libdir}/firmware/j784s4-c71_0-fw"
ALTERNATIVE_LINK_NAME[j784s4-c71_1-fw] = "${nonarch_base_libdir}/firmware/j784s4-c71_1-fw"
ALTERNATIVE_LINK_NAME[j784s4-c71_2-fw] = "${nonarch_base_libdir}/firmware/j784s4-c71_2-fw"
ALTERNATIVE_LINK_NAME[j784s4-c71_3-fw] = "${nonarch_base_libdir}/firmware/j784s4-c71_3-fw"

ALTERNATIVE_LINK_NAME[am62a-c71_0-fw] = "${nonarch_base_libdir}/firmware/am62a-c71_0-fw"

# Create the firmware alternatives

ALTERNATIVE_TARGET[j7-mcu-r5f0_1-fw] = "${INSTALL_FW_DIR}/${MCU_1_1_FW}"
ALTERNATIVE_TARGET[j7-main-r5f0_0-fw] = "${INSTALL_FW_DIR}/${MCU_2_0_FW}"
ALTERNATIVE_TARGET[j7-main-r5f0_1-fw] = "${INSTALL_FW_DIR}/${MCU_2_1_FW}"
ALTERNATIVE_TARGET[j7-main-r5f1_0-fw] = "${INSTALL_FW_DIR}/${MCU_3_0_FW}"
ALTERNATIVE_TARGET[j7-main-r5f1_1-fw] = "${INSTALL_FW_DIR}/${MCU_3_1_FW}"
ALTERNATIVE_TARGET[j7-c66_0-fw] = "${INSTALL_FW_DIR}/${C66_1_FW}"
ALTERNATIVE_TARGET[j7-c66_1-fw] = "${INSTALL_FW_DIR}/${C66_2_FW}"
ALTERNATIVE_TARGET[j7-c71_0-fw] = "${INSTALL_FW_DIR}/${C7X_1_FW}"

ALTERNATIVE_TARGET[j7-main-r5f0_0-fw-sec] = "${INSTALL_FW_DIR}/${MCU_2_0_FW}.signed"
ALTERNATIVE_TARGET[j7-main-r5f0_1-fw-sec] = "${INSTALL_FW_DIR}/${MCU_2_1_FW}.signed"
ALTERNATIVE_TARGET[j7-main-r5f1_0-fw-sec] = "${INSTALL_FW_DIR}/${MCU_3_0_FW}.signed"
ALTERNATIVE_TARGET[j7-main-r5f1_1-fw-sec] = "${INSTALL_FW_DIR}/${MCU_3_1_FW}.signed"
ALTERNATIVE_TARGET[j7-c66_0-fw-sec] = "${INSTALL_FW_DIR}/${C66_1_FW}.signed"
ALTERNATIVE_TARGET[j7-c66_1-fw-sec] = "${INSTALL_FW_DIR}/${C66_2_FW}.signed"
ALTERNATIVE_TARGET[j7-c71_0-fw-sec] = "${INSTALL_FW_DIR}/${C7X_1_FW}.signed"

ALTERNATIVE_TARGET[j721s2-mcu-r5f0_1-fw] = "${INSTALL_FW_DIR}/${MCU_1_1_FW}"
ALTERNATIVE_TARGET[j721s2-main-r5f0_0-fw] = "${INSTALL_FW_DIR}/${MCU_2_0_FW}"
ALTERNATIVE_TARGET[j721s2-main-r5f0_1-fw] = "${INSTALL_FW_DIR}/${MCU_2_1_FW}"
ALTERNATIVE_TARGET[j721s2-main-r5f1_0-fw] = "${INSTALL_FW_DIR}/${MCU_3_0_FW}"
ALTERNATIVE_TARGET[j721s2-main-r5f1_1-fw] = "${INSTALL_FW_DIR}/${MCU_3_1_FW}"
ALTERNATIVE_TARGET[j721s2-c71_0-fw] = "${INSTALL_FW_DIR}/${C7X_1_FW}"
ALTERNATIVE_TARGET[j721s2-c71_1-fw] = "${INSTALL_FW_DIR}/${C7X_2_FW}"

ALTERNATIVE_TARGET[j721s2-main-r5f0_0-fw-sec] = "${INSTALL_FW_DIR}/${MCU_2_0_FW}.signed"
ALTERNATIVE_TARGET[j721s2-main-r5f0_1-fw-sec] = "${INSTALL_FW_DIR}/${MCU_2_1_FW}.signed"
ALTERNATIVE_TARGET[j721s2-main-r5f1_0-fw-sec] = "${INSTALL_FW_DIR}/${MCU_3_0_FW}.signed"
ALTERNATIVE_TARGET[j721s2-main-r5f1_1-fw-sec] = "${INSTALL_FW_DIR}/${MCU_3_1_FW}.signed"
ALTERNATIVE_TARGET[j721s2-c71_0-fw-sec] = "${INSTALL_FW_DIR}/${C7X_1_FW}.signed"
ALTERNATIVE_TARGET[j721s2-c71_1-fw-sec] = "${INSTALL_FW_DIR}/${C7X_2_FW}.signed"

ALTERNATIVE_TARGET[j784s4-mcu-r5f0_1-fw] = "${INSTALL_FW_DIR}/${MCU_1_1_FW}"
ALTERNATIVE_TARGET[j784s4-main-r5f0_0-fw] = "${INSTALL_FW_DIR}/${MCU_2_0_FW}"
ALTERNATIVE_TARGET[j784s4-main-r5f0_1-fw] = "${INSTALL_FW_DIR}/${MCU_2_1_FW}"
ALTERNATIVE_TARGET[j784s4-main-r5f1_0-fw] = "${INSTALL_FW_DIR}/${MCU_3_0_FW}"
ALTERNATIVE_TARGET[j784s4-main-r5f1_1-fw] = "${INSTALL_FW_DIR}/${MCU_3_1_FW}"
ALTERNATIVE_TARGET[j784s4-main-r5f2_0-fw] = "${INSTALL_FW_DIR}/${MCU_4_0_FW}"
ALTERNATIVE_TARGET[j784s4-main-r5f2_1-fw] = "${INSTALL_FW_DIR}/${MCU_4_1_FW}"
ALTERNATIVE_TARGET[j784s4-c71_0-fw] = "${INSTALL_FW_DIR}/${C7X_1_FW}"
ALTERNATIVE_TARGET[j784s4-c71_1-fw] = "${INSTALL_FW_DIR}/${C7X_2_FW}"
ALTERNATIVE_TARGET[j784s4-c71_2-fw] = "${INSTALL_FW_DIR}/${C7X_3_FW}"
ALTERNATIVE_TARGET[j784s4-c71_3-fw] = "${INSTALL_FW_DIR}/${C7X_4_FW}"

ALTERNATIVE_TARGET[am62a-c71_0-fw] = "${INSTALL_FW_DIR}/${C7X_1_FW}"

ALTERNATIVE_TARGET[am62a-c71_0-fw-sec] = "${INSTALL_FW_DIR}/${C7X_1_FW}.signed"

ALTERNATIVE_PRIORITY = "50"

# make sure that lib/firmware, and all its contents are part of the package
FILES:${PN} += "${nonarch_base_libdir}/firmware"

# This is used to prevent the build system to_strip the executables
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
# This is used to prevent the build system to split the debug info in a separate file
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
# As it likely to be a different arch from the Yocto build, disable checking by adding "arch" to INSANE_SKIP
INSANE_SKIP:${PN} += "arch"

# we don't want to configure and build the source code
do_compile[noexec] = "1"
do_configure[noexec] = "1"

addtask deploy after do_install
