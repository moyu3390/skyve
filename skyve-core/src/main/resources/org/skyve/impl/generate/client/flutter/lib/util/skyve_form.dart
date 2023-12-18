import 'package:flutter/widgets.dart';
import 'package:flutter_gen_test/models/payload.dart';

class SkyveForm extends StatefulWidget {
  const SkyveForm({super.key, required this.child});

  final Widget child;

  static SkyveFormState? maybeOf(BuildContext context) {
    return context
        .dependOnInheritedWidgetOfExactType<_SkyveFormScope>()
        ?.formState;
  }

  static SkyveFormState of(BuildContext context) {
    final SkyveFormState? result = maybeOf(context);
    assert(result != null, 'No SkyveFormState found in context');
    return result!;
  }

  @override
  State<StatefulWidget> createState() {
    return SkyveFormState();
  }
}

class SkyveFormState extends State<SkyveForm> {
  /// Keep track of applied payloads, mostly for debugging
  /// though we will need the last CSRF and conversation ID
  /// later, so this might come in handy
  final _payloadHistory = <Payload>[];

  Map<String, String> metadata = {};
  Map<String, dynamic> beanValues = {};
  Map<String, String> serverErrors = {};
  int _generation = 0;

  bool get actionInProgres => false;

  String get documentName => _payloadHistory.last.documentName;

  String get moduleName => _payloadHistory.last.moduleName;

  @override
  Widget build(BuildContext context) {
    return _SkyveFormScope(
      generation: _generation,
      formState: this,
      child: widget.child,
    );
  }

  void replaceBeanValues(Map<String, dynamic> bv) {
    setState(() {
      beanValues = bv;
      _generation++;
    });
  }

  void replaceErrors(Map<String, String> errorsMap) {
    setState(() {
      serverErrors = errorsMap;
      _generation++;
    });
  }

  void removeError(String propertyKey) {
    setState(() {
      serverErrors.remove(propertyKey);
      _generation++;
    });
  }

  /// Apply the contents of the given Payload into this
  /// SkyveFormState instance
  void applyPayload(Payload payload) {
    _payloadHistory.add(payload);

    if (payload.errors.isNotEmpty) {
      replaceErrors(payload.errors);
    }

    if (payload.values.isNotEmpty) {
      replaceBeanValues(payload.values);
    }
  }

  Payload asPayload() {
    Payload lastPayload = _payloadHistory.last;

    return Payload(
        moduleName: lastPayload.moduleName,
        documentName: lastPayload.documentName,
        bizId: lastPayload.bizId,
        values: Map.of(beanValues),
        csrfToken: lastPayload.csrfToken,
        conversationId: lastPayload.conversationId);
  }
}

class _SkyveFormScope extends InheritedWidget {
  final int _generation;
  final SkyveFormState _formState;

  const _SkyveFormScope({
    required super.child,
    required generation,
    required formState,
  })  : _formState = formState,
        _generation = generation;

  SkyveFormState get formState => _formState;

  @override
  bool updateShouldNotify(_SkyveFormScope oldWidget) {
    return _generation != oldWidget._generation;
  }
}
