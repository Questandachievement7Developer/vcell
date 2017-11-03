/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.optimization.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)")
public class OptParameterValues implements org.apache.thrift.TBase<OptParameterValues, OptParameterValues._Fields>, java.io.Serializable, Cloneable, Comparable<OptParameterValues> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("OptParameterValues");

  private static final org.apache.thrift.protocol.TField PARAMETER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("parameterName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField BEST_VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("bestValue", org.apache.thrift.protocol.TType.DOUBLE, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new OptParameterValuesStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new OptParameterValuesTupleSchemeFactory();

  public java.lang.String parameterName; // required
  public double bestValue; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARAMETER_NAME((short)1, "parameterName"),
    BEST_VALUE((short)2, "bestValue");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // PARAMETER_NAME
          return PARAMETER_NAME;
        case 2: // BEST_VALUE
          return BEST_VALUE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __BESTVALUE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PARAMETER_NAME, new org.apache.thrift.meta_data.FieldMetaData("parameterName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.BEST_VALUE, new org.apache.thrift.meta_data.FieldMetaData("bestValue", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(OptParameterValues.class, metaDataMap);
  }

  public OptParameterValues() {
  }

  public OptParameterValues(
    java.lang.String parameterName,
    double bestValue)
  {
    this();
    this.parameterName = parameterName;
    this.bestValue = bestValue;
    setBestValueIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public OptParameterValues(OptParameterValues other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetParameterName()) {
      this.parameterName = other.parameterName;
    }
    this.bestValue = other.bestValue;
  }

  public OptParameterValues deepCopy() {
    return new OptParameterValues(this);
  }

  @Override
  public void clear() {
    this.parameterName = null;
    setBestValueIsSet(false);
    this.bestValue = 0.0;
  }

  public java.lang.String getParameterName() {
    return this.parameterName;
  }

  public OptParameterValues setParameterName(java.lang.String parameterName) {
    this.parameterName = parameterName;
    return this;
  }

  public void unsetParameterName() {
    this.parameterName = null;
  }

  /** Returns true if field parameterName is set (has been assigned a value) and false otherwise */
  public boolean isSetParameterName() {
    return this.parameterName != null;
  }

  public void setParameterNameIsSet(boolean value) {
    if (!value) {
      this.parameterName = null;
    }
  }

  public double getBestValue() {
    return this.bestValue;
  }

  public OptParameterValues setBestValue(double bestValue) {
    this.bestValue = bestValue;
    setBestValueIsSet(true);
    return this;
  }

  public void unsetBestValue() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __BESTVALUE_ISSET_ID);
  }

  /** Returns true if field bestValue is set (has been assigned a value) and false otherwise */
  public boolean isSetBestValue() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __BESTVALUE_ISSET_ID);
  }

  public void setBestValueIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __BESTVALUE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case PARAMETER_NAME:
      if (value == null) {
        unsetParameterName();
      } else {
        setParameterName((java.lang.String)value);
      }
      break;

    case BEST_VALUE:
      if (value == null) {
        unsetBestValue();
      } else {
        setBestValue((java.lang.Double)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case PARAMETER_NAME:
      return getParameterName();

    case BEST_VALUE:
      return getBestValue();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case PARAMETER_NAME:
      return isSetParameterName();
    case BEST_VALUE:
      return isSetBestValue();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof OptParameterValues)
      return this.equals((OptParameterValues)that);
    return false;
  }

  public boolean equals(OptParameterValues that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_parameterName = true && this.isSetParameterName();
    boolean that_present_parameterName = true && that.isSetParameterName();
    if (this_present_parameterName || that_present_parameterName) {
      if (!(this_present_parameterName && that_present_parameterName))
        return false;
      if (!this.parameterName.equals(that.parameterName))
        return false;
    }

    boolean this_present_bestValue = true;
    boolean that_present_bestValue = true;
    if (this_present_bestValue || that_present_bestValue) {
      if (!(this_present_bestValue && that_present_bestValue))
        return false;
      if (this.bestValue != that.bestValue)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetParameterName()) ? 131071 : 524287);
    if (isSetParameterName())
      hashCode = hashCode * 8191 + parameterName.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(bestValue);

    return hashCode;
  }

  @Override
  public int compareTo(OptParameterValues other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetParameterName()).compareTo(other.isSetParameterName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParameterName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.parameterName, other.parameterName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetBestValue()).compareTo(other.isSetBestValue());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBestValue()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.bestValue, other.bestValue);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("OptParameterValues(");
    boolean first = true;

    sb.append("parameterName:");
    if (this.parameterName == null) {
      sb.append("null");
    } else {
      sb.append(this.parameterName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("bestValue:");
    sb.append(this.bestValue);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (parameterName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'parameterName' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'bestValue' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class OptParameterValuesStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OptParameterValuesStandardScheme getScheme() {
      return new OptParameterValuesStandardScheme();
    }
  }

  private static class OptParameterValuesStandardScheme extends org.apache.thrift.scheme.StandardScheme<OptParameterValues> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, OptParameterValues struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARAMETER_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.parameterName = iprot.readString();
              struct.setParameterNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BEST_VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.bestValue = iprot.readDouble();
              struct.setBestValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetBestValue()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'bestValue' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, OptParameterValues struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.parameterName != null) {
        oprot.writeFieldBegin(PARAMETER_NAME_FIELD_DESC);
        oprot.writeString(struct.parameterName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(BEST_VALUE_FIELD_DESC);
      oprot.writeDouble(struct.bestValue);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OptParameterValuesTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OptParameterValuesTupleScheme getScheme() {
      return new OptParameterValuesTupleScheme();
    }
  }

  private static class OptParameterValuesTupleScheme extends org.apache.thrift.scheme.TupleScheme<OptParameterValues> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, OptParameterValues struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.parameterName);
      oprot.writeDouble(struct.bestValue);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, OptParameterValues struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.parameterName = iprot.readString();
      struct.setParameterNameIsSet(true);
      struct.bestValue = iprot.readDouble();
      struct.setBestValueIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
