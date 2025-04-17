// utility.c

#include "utility.h"

int find_min(int a, int b) {
    return (a < b) ? a : b;
}

int find_max(int a, int b) {
    return (a > b) ? a : b;
}

int compare_ints(int a, int b) {
    if (a < b) return -1;
    else if (a > b) return 1;
    else return 0;
}
